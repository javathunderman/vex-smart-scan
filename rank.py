import previousseasons
import requests
import json
import operator
import re
from collections import OrderedDict
sku = input("SKU\n")
rawTeams = requests.get("https://api.vexdb.io/v1/get_rankings?sku="+sku)
cleanTeams = rawTeams.json()
rawSeason = requests.get("https://api.vexdb.io/v1/get_events?sku="+sku) #REE VEXDB
cleanSeason = rawSeason.json()
season = cleanSeason['result'][0]['season']
finalList = {}
for team in cleanTeams['result']:
    awardTotal = 0
    skillList = []
    CCVM = team['ccwm']
    ap = team['ap']
    sp = team['sp']
    wp = team['wp']
    CCVM = team['ccwm']
    pCCVM = (previousseasons.lastyear(sku, team['team']))
    twopCCVM = (previousseasons.twoyears(sku, team['team']))
    rawAwards = requests.get("https://api.vexdb.io/v1/get_awards?team=" + team['team'] + "&season="+season)
    cleanAwards = rawAwards.json()
    for award in cleanAwards['result']:
        if("Excellence" in award['name']):
            awardTotal+=10
        elif("Champion" in award['name']):
            awardTotal+=8
        elif("Judges" in award['name']):
            awardTotal+=2
        else:
            awardTotal+=6
    rawSkills = requests.get("https://api.vexdb.io/v1/get_skills?team=" + team['team'] + "&sku=" + sku)
    cleanSkills = rawSkills.json()
    for skill in cleanSkills['result']:
        skillList.append(skill['score'])
    try:
        maxSkill = max(skillList)
    except ValueError:
        maxSkill = 0
    finalScore = (3*CCVM) + (1.5*awardTotal) + 1.2*(pCCVM + twopCCVM) + (2*maxSkill) + (0.3*wp) + (0.2*(ap+sp))
    finalList[team['team']] = int(finalScore)
finalList = OrderedDict(sorted(finalList.items(), key=lambda kv: kv[1], reverse=True))
for key, value in finalList.items():
    print(key, value)
with open('result.json', 'w') as fp:
    json.dump(finalList, fp)
