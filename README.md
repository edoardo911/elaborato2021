<h2>Progetto dell'elaborato della maturità 2021 di Edoardo Stucchi, Carmen Catucci e Samuele Floris</h2>
La cartella src contiene i file dell'app, progettata su android studio in java e descritta tramite xml.
L'applicazione consente all'utente di eseguire un login in due modi diversi, uno tramite SPID e uno tramite tessera NFC.
Una volta eseguito il login, all'utente verranno chieste le proprie malattie pregresse che saranno usate in un algoritmo del backend per
calcolare il tipo di vaccino al quale si dovà sottoporre. Inoltre tramite l'applicazione è possibile autenticare l'account creato sul sito
tramite la tessera NFC, che autenticherà solo un ristretto gruppo di persone.
La cartella sito-comune contiene i file che riguardano il sito del comune. Il sito è stato realizzato con Angular, un framework di javascript
programmato prevalentemente in TypeScript, un linguaggio moderno realizzato sulle basi di javascript stesso. Il sito è inoltre descritto usando
HTML, CSS e vari JSON di configurazione. Il sito comunica con firebase, un sito che fornisce database non relazionali sul quale memorizziamo i
dati degli utenti e dei vaccinati. Si presenta come una pagina che consente sia login sia registrazione. In seguito, se l'account sarà stato autenticato
dall'app, consentirà la visualizzazione delle pagine statistiche, dove l'utente potrà vedere in due pagine diverse rispettivamente tutti gli utenti
vaccinati e dei grafici statistici riguardo gli utenti vaccinati. Al momento il sito si trova su vaccinationdata.duckdns.org.
