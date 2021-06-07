import { ChartDataSets, ChartOptions, ChartTitleOptions, ChartType } from "chart.js";
import { AngularFirestore, validateEventsArray } from "@angular/fire/firestore";
import { CookieService } from "ngx-cookie-service";
import { Label, SingleDataSet } from "ng2-charts";
import { Component } from "@angular/core";
import { Router } from "@angular/router";

@Component({
    templateUrl: "./graph.component.html",
    styleUrls: ["./graph.component.css"]
})
export class GraphComponent
{
    //età
    chartOptions: ChartOptions = {
        responsive: true,
        scales: {
            yAxes: [{
                ticks: {
                    stepSize: 1,
                    min: 0
                }
            }]
        }
    };

    barChartLabels: Label[] = [];
    barChartData: ChartDataSets[] = [{
        data: [], label: 'Numero vaccinati per età', backgroundColor: "#00EE00", hoverBackgroundColor: "#00AA00"
    }];

    //genere
    genderOptions: ChartOptions = {
        responsive: true,
        tooltips: {
            callbacks: {
               label: (index, data) => { return data.labels[index.index][0] + ": " + data.datasets[0].data[index.index] + "%" }
            }
         },
    };

    pieChartLabels: Label[] = [];
    pieChartData: SingleDataSet = [];
    pieChartColors: Array<any> = [{
        backgroundColor: [],
     }];

    //dosi
    dosiOptions: ChartOptions = {
        responsive: true,
        scales: {
            yAxes: [{
                ticks: {
                    stepSize: 1,
                    min: 0
                }
            }]
        },
        tooltips: {
            callbacks: {
               title: () => { return "" }
            }
         },
    };
    dosiLabels: Label[] = [];
    dosiData: ChartDataSets[] = [
        { data: [], label: 'Nessuna dose', backgroundColor: "#FF0055", hoverBackgroundColor: "#AA0033" },
        { data: [], label: 'Prima dose', backgroundColor: "#4434F5", hoverBackgroundColor: "#2212A3" },
        { data: [], label: 'Entrambe le dosi', backgroundColor: "#DE8F44", hoverBackgroundColor: "#995A11" },
        { data: [], label: 'Johnson & Johnson', backgroundColor: "#7F30E6", hoverBackgroundColor: "#5A1093" },
    ];

    constructor(router:Router, cs:CookieService, private firestore:AngularFirestore)
    {
        if(cs.get("email") == "")
            router.navigateByUrl("/login")
        firestore.collection("users", ref => ref.where("email", "==", cs.get("email"))).get().subscribe(data => {
            if(!data.docs[0].data()["auth"])
                router.navigateByUrl("/login")
        });

        this.firestore.collection("vaccinati").get().subscribe(data => {
            let mappaEta = new Map<number, number>();
            let tot = 0;
            let totM = 0;
            let totF = 0;
            let totA = 0;
            let noDose = 0;
            let dose1 = 0;
            let dose2 = 0;
            let jj = 0;
            data.forEach(value => {
                if(value.data()["vaccino"] != "")
                {
                    let eta = value.data()["eta"];
                    if(!mappaEta.has(eta))
                        mappaEta.set(eta, 1);
                    else
                        mappaEta.set(eta, mappaEta.get(eta) + 1);

                    if(value.data()["genere"] == "M")
                        totM++;
                    else if(value.data()["genere"] == "F")
                        totF++;
                    else
                        totA++;
                    tot++;

                    if(value.data()["vaccino"] == "J&J" && this.fattoVaccino(value.data()["dataVaccino1"]))
                        jj++;
                    else if(this.fattoVaccino(value.data()["dataVaccino1"]))
                    {
                        if(this.fattoVaccino(value.data()["dataVaccino2"]))
                            dose2++;
                        else
                            dose1++;
                    }
                    else noDose++;
                }
            });
            mappaEta = this.ordinaEta(mappaEta);
            mappaEta.forEach((key, value) => {
                this.barChartData[0].data.push(key);
                this.barChartLabels.push(value.toString());
            });

            this.setPieData(totF, totM, totA, tot);

            this.dosiData[0].data.push(noDose);
            this.dosiData[1].data.push(dose1);
            this.dosiData[2].data.push(dose2);
            this.dosiData[3].data.push(jj);
        });
    }

    private ordinaEta(mappaEta:Map<number, number>):Map<number, number>
    {
        let sorted = new Map<number, number>();
        let keys = mappaEta.keys();
        let stringKeys = [];
        let val;

        while(!(val = keys.next()).done) stringKeys.push(val.value);
        stringKeys.sort().forEach(value => { sorted.set(value, mappaEta.get(value)) });

        return sorted;
    }
    
    private setPieData(totF:number, totM:number, totA:number, tot):void
    {
        if(totF > 0)
        {
            this.pieChartColors[0].backgroundColor.push("pink");
            this.pieChartLabels.push(["Donna"]);
            this.pieChartData.push((totF / tot) * 100);
        }
        if(totM > 0)
        {
            this.pieChartColors[0].backgroundColor.push("blue");
            this.pieChartLabels.push(["Uomo"]);
            this.pieChartData.push((totM / tot) * 100);
        }
        if(totA > 0)
        {
            this.pieChartColors[0].backgroundColor.push("yellow");
            this.pieChartLabels.push(["Altro"]);
            this.pieChartData.push((totA / tot) * 100);
        }
    }

    public fattoVaccino(data:string):boolean
    {
        let vars = data.split("/") as string[];
        return new Date(vars[2] + "-" + vars[1] + "-" + vars[0]).getTime() < Date.now();
    }
}
