from datetime import timedelta
from datetime import datetime
from datetime import date
import pdfkit
import json

# functions

# vaccino
def get_vaccino():
  # ciò  che fa questa funzione è una mini-analisi statistica per ottenere il miglior vaccino somministrabile all'user
  # in base alle sue allergie, intolleranze, malattie
  type = { 'p': False, 'm': False, 'a': False, 'j': False } # dict dei valori di ogni vaccino
  # (al termine di tutti i calcoli della funzione, il vaccino che avrà il valore
  # più alto all'interno del dizionario sarà il vaccino ottimale per l'user)

  # se dovessero tutti a False significa che all'user non può essere somministrato alcun vaccino 
  nota = [] # eventuali note per l'user
  ALERT_CONSERVANTI = 'Data la sua intolleranza ai conservanti, presti attenzione le ore e i giorni successivi al vaccino per eventuali leggeri effetti collaterali.'
  ALERT_DIABETE     = 'Data la sua positività al diabete, è consigliato tenere sotto controllo il livello di glicemia nel sangue le ore e i giorni successivi al vaccino.'
  ALERT_SACCAROSIO  = 'Data la sua intolleranza al saccarosio, presti attenzione le ore e i giorni successivi al vaccino per eventuali leggeri effetti collaterali.'
  ALERT_VISITA      = 'Si consiglia di sottoporsi ad una visita allergologica prima di procedere con la somministrazione del vaccino.'

  if user['anafilassi'] or user['asma'] or user['mastocitosi'] or user['polisorbati']: # se si hanno queste malattie, il vaccino non va fatto
    nota.append(ALERT_VISITA)
    return [type, nota]

  if user['eta'] < 18:      # (..., 18) - controllo sull'età
    type['p'] = 1
    if user['conservanti']:
      nota.append(ALERT_CONSERVANTI)
    if user['diabete']:
      nota.append(ALERT_DIABETE)
    if user['saccarosio']:
      nota.append(ALERT_SACCAROSIO)
    return[type, nota]

  else:
    if user['eta'] < 60:    # [18, 60) - controllo sull'età
      type['p'] = 1
      type['m'] = 3
      type['a'] = 2
      type['j'] = 1
    else:                   # [60, ...) - controllo sull'età
      type['p'] = 1
      type['m'] = 2
      type['a'] = 3
      type['j'] = 3

    # NO Moderna
    if user['polietilenico'] or user['trometamina']: type['m'] = False

    # NO AstraZeneca
    if user['polietilenico'] or user['agrumi'] or user['insf_renale'] or user['colite_ulcerosa']: type['a'] = False

    # + Johnson&Johnson
    if user['conservanti']:
      if user['eta'] < 60: type['j'] += 3
      else: type['j'] += 1

    # NO Johnson&Johnson
    if user['polietilenico'] or user['agrumi']: type['j'] = False

    max_key = max(type, key=type.get)
    # se il vaccino è uno di questi (Pfizer, Moderna, AstraZeneca) bisogna mettere delle note
    # dato che Johnson&Johnson non ha controindicazioni per conservanti e saccarosio 
    if max_key == 'p' or max_key == 'm' or max_key == 'a':
      if user['conservanti']: nota.append(ALERT_CONSERVANTI)
      if user['saccarosio']: nota.append(ALERT_SACCAROSIO)
    # per il diabete stare attenti qualsiasi sia il vaccino
    if user['diabete']: nota.append(ALERT_DIABETE)

    # ritorna dizionario risultante dall'analisi dei vaccini e le eventuali note
    return [type, nota]


# date
def get_date(vac):
  global data_1
  global ora_1
  global sala_1
  global data_2
  global ora_2
  global sala_2
  # calcolo della data iniziale per la ricerca dell'orario ottimale per l'user
  if user['gravidanza']:
    data_1 = datetime.strptime(user['gravidanza'], '%d/%m/%Y').date() + timedelta(days=30*17)
  elif user['allattamento']:
    data_1 = datetime.strptime(user['allattamento'], '%d/%m/%Y').date() + timedelta(days=30*7)
  else:
    data_1 = date.today() + timedelta(days=10)

  data_1, ora_1, sala_1 = set_data(data_1, 1)

  if vac == 'p':
    data_2 = datetime.strptime(data_1, "%d/%m/%Y").date() + timedelta(days=21)
  elif vac == 'm':
    data_2 = datetime.strptime(data_1, "%d/%m/%Y").date() + timedelta(days=28)
  elif vac == 'a':
    data_2 = datetime.strptime(data_1, "%d/%m/%Y").date() + timedelta(days=42)

  if vac != 'j':
    data_2, ora_2, sala_2 = set_data(data_2, 2)


def set_data(data, dose):
  # dato che ci sono 3 lobby al momento nell'ipotetica sede dei vaccini covid
  # questo ciclo permette di assegnare all'utente la data e l'ora migliore
  while True:                         # ciclo per ricerca di orario ottimale
    turn_list = False                 # variabile d'appoggio per ottenere la lista di orari nel turno con meno posti
    data = data.strftime('%d/%m/%Y')

    if data in date_vac:
      f = 0                             # flag per verificare se nel ciclo for successivo la key è la prima lobby del dict
      min_lobby = False                 # la lobby con meno orari al suo interno, quindi la meno occupata
                      
      for lobby in date_vac[data]:    # for per verificare qual'è la lobby piu libera in quella data
        if f == 0:                      # controllo con il flag per assegnare a min_lobby la prima lobby se è il primo ciclo
          min_lobby = lobby             
          f = 1                         # aggiornamento del flag a 1 cosicchè non entri al prossimo ciclo
          continue        
        if len(date_vac[data][lobby]) < len(date_vac[data][min_lobby]):       # calcolo lobby con meno orari
          min_lobby = lobby

      if len(date_vac[data][min_lobby]) == 0:
        # se la lobby con meno posti ha ancora 0 prenotazioni, si aggiunge la prima alle 08:00
        set_dict(data, min_lobby, '08:00', user['email'], dose)
        return [data, '08:00', min_lobby]

      else:
        turn_list = list(date_vac[data][min_lobby].keys())
        
        if '08:00' not in turn_list: 
          set_dict(data, min_lobby, '08:00', user['email'], dose)
          sorted_items = sorted(date_vac[data][min_lobby].items())
          date_vac[data][min_lobby] = dict(sorted_items)
          return [data, '08:00', min_lobby]

        ff = 0
        for time in turn_list:
          next_time = (datetime.strptime(time, "%H:%M") + timedelta(seconds=60*5)).strftime("%H:%M")
           
          if next_time not in date_vac[data][min_lobby] and next_time != '20:00':
            set_dict(data, min_lobby, next_time, user['email'], dose)
            return [data, next_time, min_lobby]

          elif next_time == '20:00':
            data = datetime.strptime(data, '%d/%m/%Y').date() + timedelta(days=1)
            ff = 1
            break

        if ff == 1:
          sorted_items = sorted(date_vac[data][min_lobby].items())
          date_vac[data][min_lobby] = dict(sorted_items)
          break

    else: # se la data NON È presente nel dict
      date_vac[data] = {}
      date_vac[data]['1'] = {}
      set_dict(data, '1', '08:00', user['email'], dose)
      date_vac[data]['2'] = {}
      date_vac[data]['3'] = {}
      return [data, '08:00', '1']


def set_dict(data, lobby, hour, email, dose):
  date_vac[data][lobby][hour] = []
  date_vac[data][lobby][hour].append(email)
  date_vac[data][lobby][hour].append(dose)



# pdf
def create_pdf():
  options = {
    'page-size':'A4',
    'encoding':'utf-8', 
    'margin-top':'0cm',
    'margin-bottom':'0cm',
    'margin-left':'0cm',
    'margin-right':'0cm',
    'dpi': 300,
  }

  # read
  with open('pdf/index.html', 'r') as file: 
    html = file.read()

  html = html.replace('IN:NOME', user['nome'])
  html = html.replace('IN:COGNOME', user['cognome'])
  html = html.replace('IN:ETA', str(user['eta']))
  html = html.replace('IN:EMAIL', user['email'])

  if vaccino == 'p':
    html = html.replace('IN:TIPOLOGIA', 'Pfizer')
  elif vaccino == 'm':
    html = html.replace('IN:TIPOLOGIA', 'Moderna')
  elif vaccino == 'a':
    html = html.replace('IN:TIPOLOGIA', 'AstraZeneca')
  elif vaccino == 'j':
    html = html.replace('IN:TIPOLOGIA', 'Johnson & Johnson')

  html = html.replace('IN:DATA-1', data_1)
  html = html.replace('IN:ORA-1', ora_1)
  html = html.replace('IN:SALA-1', sala_1)

  if vaccino == 'j':
    html = html.replace('IN:JJ-1', '<!--')
    html = html.replace('IN:JJ-2', '-->')
  else:
    html = html.replace('IN:JJ-1', '')
    html = html.replace('IN:JJ-2', '')
    html = html.replace('IN:DATA-2', data_2)
    html = html.replace('IN:ORA-2', ora_2)
    html = html.replace('IN:SALA-2', sala_2)

  if not note:
    html = html.replace('IN:NOTE', '')
  else:
    s = '<div class="notes"><p><b>Note per il dopo-vaccino</b>:</p><ul>'
    for nota in note:
      s = s + '<li>' + nota + '</li>'
    html = html.replace('IN:NOTE', s + '</ul>')

  # write
  with open('pdf/pdf.html', 'w') as file: 
    file.write(html)

  pdfkit.from_file('pdf/pdf.html', 'pdf/out.pdf', options=options)


# main
user        = False
vaccino     = False
data_1      = False
ora_1       = False
sala_1      = False
data_2      = False
ora_2       = False
sala_2      = False

def elaborate():
  # lettura dei file
  with open('user.json', 'r') as file: 
    global user
    user = json.load(file)

  with open('date.json', 'r') as file:
    global date_vac
    date_vac = json.load(file)

  # ricerca vaccino ottimale
  global note
  vaccino_dict, note = get_vaccino()
  if max(vaccino_dict.values()):
    vaccino = max(vaccino_dict, key=vaccino_dict.get) # vaccino risultante dalla mini-analisi statistica

  if vaccino == 'p':
    nome_vaccino = 'Pfizer'
  elif vaccino == 'a':
    nome_vaccino = 'AstraZeneca'
  elif vaccino == 'm':
    nome_vaccino = 'Moderna'
  elif vaccino == 'j':
    nome_vaccino = 'Johnson & Johnson'
  else:
    nome_vaccino = ''

  # ricerca date 
  if vaccino:
    get_date(vaccino)
  # scrittura su date.json
  with open('date.json', 'w') as file:
    json.dump(date_vac, file, indent=2)

  # creazione pdf
  if vaccino:
    create_pdf()

  # creazione json finale
  if vaccino:
    final_json  = {"dataVaccino1":data_1, "dataVaccino2":data_2, "vaccino": nome_vaccino}
  else:
    final_json  = {"dataVaccino1":"", "dataVaccino2":"", "vaccino": nome_vaccino}

  final_json = str(final_json)
  final_json = final_json.replace('\'', '\"')
  return final_json