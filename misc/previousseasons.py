import requests
import json
import re
import datetime
def cleanDate(rawDate):
  datelist = re.split(r'\s|-', rawDate)
  rawDay = datelist[2]
  rawDay = rawDay[0:datelist[2].find("T")]
  datelist.pop(2)
  datelist.append(rawDay)
  return(datelist)
def nearest(teamEvents, oldDate):
    return min(teamEvents, key=lambda x: abs(x - oldDate))
def lastyear(sku, team):
  teamEvents = []
  rawRes = requests.get("https://api.vexdb.io/v1/get_events?sku="+sku)
  parseInit = rawRes.json()
  rawDate = parseInit['result'][0]['start']
  currentdate = cleanDate(rawDate)
  oldDate = [str(int(currentdate[0])-1)] + currentdate[1:]
  oldDate = datetime.datetime(int(oldDate[0]), int(oldDate[1]), int(oldDate[2]))
  #print(oldDate)
  #print(newdatelist)

  rawTeam = requests.get("https://api.vexdb.io/v1/get_events?team="+team)
  parseTeam = rawTeam.json()
  for event in parseTeam['result']:
      eachEvent = (cleanDate(event['start']))
      teamEvents.append(datetime.datetime(int(eachEvent[0]), int(eachEvent[1]), int(eachEvent[2])))
  previousYear = nearest(teamEvents, oldDate).isoformat()
  #print(previousYear)
  pRawEvent = requests.get("https://api.vexdb.io/v1/get_events?team="+team+"&date=" +previousYear)
  pYearParsed = pRawEvent.json()
  pSKU = pYearParsed['result'][0]['sku']
  pRawRank = requests.get("https://api.vexdb.io/v1/get_rankings?team="+team + "&sku="+pSKU)
  pParsedRank = pRawRank.json()
  try:
      pCCVM = pParsedRank['result'][0]['ccwm']
  except IndexError:
      pCCVM = 0
  return(pCCVM)
def twoyears(sku, team):
  teamEvents = []
  rawRes = requests.get("https://api.vexdb.io/v1/get_events?sku="+sku)
  parseInit = rawRes.json()
  rawDate = parseInit['result'][0]['start']
  currentdate = cleanDate(rawDate)
  oldDate = [str(int(currentdate[0])-2)] + currentdate[1:]
  oldDate = datetime.datetime(int(oldDate[0]), int(oldDate[1]), int(oldDate[2]))
  #print(oldDate)
  #print(newdatelist)

  rawTeam = requests.get("https://api.vexdb.io/v1/get_events?team="+team)
  parseTeam = rawTeam.json()
  for event in parseTeam['result']:
      eachEvent = (cleanDate(event['start']))
      teamEvents.append(datetime.datetime(int(eachEvent[0]), int(eachEvent[1]), int(eachEvent[2])))
  previousYear = nearest(teamEvents, oldDate).isoformat()
  #print(previousYear)
  pRawEvent = requests.get("https://api.vexdb.io/v1/get_events?team="+team+"&date=" +previousYear)
  pYearParsed = pRawEvent.json()
  pSKU = pYearParsed['result'][0]['sku']
  pRawRank = requests.get("https://api.vexdb.io/v1/get_rankings?team="+team + "&sku="+pSKU)
  pParsedRank = pRawRank.json()
  try:
      pCCVM = pParsedRank['result'][0]['ccwm']
  except IndexError:
      pCCVM = 0
  return(pCCVM)
