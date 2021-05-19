export class Vaccinato
{
    public cittaNascita:string;
    public codiceFiscale:string;
    public cognome:string;
    public dataNascita:string;
    public dataVaccino1:string;
    public dataVaccino2:string;
    public genere:string;
    public indirizzo:string;
    public nazioneNascita:string;
    public nome:string;
    public provincia:string;
    public residenza:string;
    public vaccino:string;

    constructor(cittaNascita:string, codiceFiscale:string, cognome:string, dataNascita:string, dataVaccino1:string, dataVaccino2:string,
        genere:string, indirizzo:string, nazioneNascita:string, nome:string, provincia:string, residenza:string, vaccino:string)
    {
        this.cittaNascita = cittaNascita;
        this.codiceFiscale = codiceFiscale;
        this.cognome = cognome;
        this.dataNascita = dataNascita;//23 03 84
        this.dataVaccino1 = dataVaccino1;
        this.dataVaccino2 = dataVaccino2;
        this.genere = genere;
        this.indirizzo = indirizzo;
        this.nazioneNascita = nazioneNascita;
        this.nome = nome;
        this.provincia = provincia;
        this.residenza = residenza;
        this.vaccino = vaccino;
    }
}