import previousseasons
import requests
import json
import operator
import re
from collections import OrderedDict
sku = input("SKU\n") # Get the initial SKU (tournament UID) from user
rawTeams = requests.get("https://api.vexdb.io/v1/get_rankings?sku="+sku) # get team list
cleanTeams = rawTeams.json()
rawSeason = requests.get("https://api.vexdb.io/v1/get_events?sku="+sku) # get season (REE VEXDB WHY DOES THIS NEED TO BE IN ANOTHER API CALL)
cleanSeason = rawSeason.json()
season = cleanSeason['result'][0]['season'] # define season var
finalList = {} # create output dictionary for team numbers and power scores
for team in cleanTeams['result']: # for each team at the event
    awardTotal = 0 # create award total
    skillList = [] # create list of skills scores from a tournament
    CCVM = team['ccwm'] # get tournament CCVM/CCWM
    ap = team['ap'] # get autonomous points
    sp = team['sp'] # get special points
    wp = team['wp'] # get win points
    pCCVM = (previousseasons.lastyear(sku, team['team'])) # use previousseasons methods to obtain previous year's CCVM/CCWM
    twopCCVM = (previousseasons.twoyears(sku, team['team']))
    rawAwards = requests.get("https://api.vexdb.io/v1/get_awards?team=" + team['team'] + "&season="+season) # get awards won by a team
    cleanAwards = rawAwards.json()
    for award in cleanAwards['result']: # add points to awardTotal based on award (+10 for champs, +8 for excel, +2 for judges, +3 for semis, +6 for everything else)
        rawStates = requests.get("https://api.vexdb.io/v1/get_events?sku="+award['sku']) # get type of award won by a team
        cleanstates = rawStates.json()
        if("Excellence" in award['name']):
            if("state" in cleanstates['result'][0]['name'].lower() or "regionals" in cleanstates['result'][0]['name'].lower() or "signature event" in cleanstates['result'][0]['name'].lower() or "nationals" or "regionals" in cleanstates['result'][0]['name'].lower()): #checks if award is from state/regional/sigevent/national comp
                awardTotal+=(8*1.3)

            else:
                awardTotal+=8

        elif("Champion" in award['name']):
            if("state" in cleanstates['result'][0]['name'].lower() or "regionals" in cleanstates['result'][0]['name'].lower() or "signature event" in cleanstates['result'][0]['name'].lower() or "nationals" or "regionals" in cleanstates['result'][0]['name'].lower()): #checks if award is from state/regional/sigevent/national comp
                awardTotal+=(10*1.3)

            else:
                awardTotal+=10

        elif("Judges" in award['name']):
            if("state" in cleanstates['result'][0]['name'].lower() or "regionals" in cleanstates['result'][0]['name'].lower() or "signature event" in cleanstates['result'][0]['name'].lower() or "nationals" or "regionals" in cleanstates['result'][0]['name'].lower()): #checks if award is from state/regional/sigevent/national comp
                awardTotal+=(2*1.3)

            else:
                awardTotal+=2

        elif("Semifinalists" in award['name']):
            if("state" in cleanstates['result'][0]['name'].lower() or "regionals" in cleanstates['result'][0]['name'].lower() or "signature event" in cleanstates['result'][0]['name'].lower() or "nationals" or "regionals" in cleanstates['result'][0]['name'].lower()): #checks if award is from state/regional/sigevent/national comp
                awardTotal+=(3*1.3)

            else:
                awardTotal+=3

        else:
            if("state" in cleanstates['result'][0]['name'].lower() or "regionals" in cleanstates['result'][0]['name'].lower() or "signature event" in cleanstates['result'][0]['name'].lower() or "nationals" or "regionals" in cleanstates['result'][0]['name'].lower()): #checks if award is from state/regional/sigevent/national comp
                awardTotal+=(6*1.3)

            else:
                awardTotal+=6

    rawSkills = requests.get("https://api.vexdb.io/v1/get_skills?team=" + team['team'] + "&sku=" + sku) # get raw skills scores
    cleanSkills = rawSkills.json()
    for skill in cleanSkills['result']:
        skillList.append(skill['score'])
    try:
        maxSkill = max(skillList) # get highest skills score for a team at a comp
    except ValueError:
        maxSkill = 0
    finalScore = (3*CCVM) + (1.5*awardTotal) + 1.2*(pCCVM + twopCCVM) + (2*maxSkill) + (0.3*wp) + (0.2*(ap+sp)) # calaculate power score
    finalList[team['team']] = int(finalScore) # add to dictionary
finalList = OrderedDict(sorted(finalList.items(), key=lambda kv: kv[1], reverse=True)) # sort dict in descending order

for key, value in finalList.items():
    print(key, value) # print each team out
with open('result.json', 'w') as fp:
    json.dump(finalList, fp) # dump OrderedDict into JSON (for API purposes)
