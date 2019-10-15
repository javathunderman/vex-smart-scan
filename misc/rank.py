import previousseasons
import requests
import json
import operator
import re
from collections import OrderedDict
def main():
    total = 0
    sku = input("SKU\n") # Get the initial SKU (tournament UID) from user
    runOnce = True
    rawTeams = requests.get("https://api.vexdb.io/v1/get_rankings?sku="+sku) # get team list
    cleanTeams = rawTeams.json()
    rawSeason = requests.get("https://api.vexdb.io/v1/get_events?sku="+sku) # get season (REE VEXDB WHY DOES THIS NEED TO BE IN ANOTHER API CALL)
    cleanSeason = rawSeason.json()
    season = cleanSeason['result'][0]['season'] # define season var
    finalList = {} # create output dictionary for team numbers and power scores
    if(cleanTeams['size'] > 0):
        for team in cleanTeams['result']: # for each team at the event
            CCVM = team['ccwm'] # get tournament CCVM/CCWM
            ap = team['ap'] # get autonomous points
            sp = team['sp'] # get special points
            wp = team['wp'] # get win points
            trsp = cleanOEvents['result'][0]['trsp']
            pCCVM = (previousseasons.lastyear(sku, team['team'])) # use previousseasons methods to obtain previous year's CCVM/CCWM
            twopCCVM = (previousseasons.twoyears(sku, team['team']))
            skills = skillsCheck(team, sku)
            awards = awardsCheck(team, season)
            finalScore = (3*CCVM) + (1.5*awards) + 1.5*(pCCVM + twopCCVM) + (3*skills) + (0.3*wp) + (0.2*(ap+sp)) # calaculate power score
            finalList[team['team']] = int(finalScore) # add to dictionary
    else:
        rawTeams = requests.get("https://api.vexdb.io/v1/get_teams?sku="+sku) # get team list
        cleanTeams = rawTeams.json()
        for team in cleanTeams['result']:
            rawPreviousEvents = requests.get("https://api.vexdb.io/v1/get_events?team="+team['number']+"&season="+season) # Fetch a team's list of events for season
            cleanPEvents = rawPreviousEvents.json()
            #print("https://api.vexdb.io/v1/get_events?team="+team['number']+"&season="+season)
            for event in cleanPEvents['result']: # for each event in the list of previous competitions
                #print(event)
                if(event['sku'] != sku and runOnce): # grab the SKU and check that it is not the same as the initial tournament we're processing
                    #print(event['sku'])
                    rawOldEvents = requests.get("https://api.vexdb.io/v1/get_rankings?sku="+event['sku']+"&team="+team['number']) # get team list for previosu comp
                    cleanOEvents = rawOldEvents.json()
                    if(cleanOEvents['size'] == 1):
                        CCVM = cleanOEvents['result'][0]['ccwm']
                        wp = cleanOEvents['result'][0]['wp']
                        ap = cleanOEvents['result'][0]['ap']
                        sp = cleanOEvents['result'][0]['sp']
                        trsp = cleanOEvents['result'][0]['trsp']
                        pCCVM = (previousseasons.lastyear(event['sku'], team['number'])) # grab pCCVM twopCCVM
                        twopCCVM = (previousseasons.twoyears(event['sku'], team['number']))
                        skills = skillsCheck(team['number'],event['sku']) # grab skills at that event
                        awards = awardsCheck(team['number'], season) # grab awards
                        #print(CCVM, wp, ap, sp, pCCVM, twopCCVM, skills, awards)
                        if(skills is None):
                            skills = 0
                        if(awards is None):
                            awards = 0
                        finalScore = (3*CCVM) + (1.5*awards) + 1.5*(pCCVM + twopCCVM) + (3*skills) + (0.3*wp) + (0.2*(ap+sp)) # calaculate power score
                        finalList[team['number']] = int(finalScore) # add to dictionary
                        total+=1
                        runOnce = False
                    else:
                        pass
            runOnce = True
    finalList = OrderedDict(sorted(finalList.items(), key=lambda kv: kv[1], reverse=True)) # sort dict in descending order
    print(total)
    for key, value in finalList.items():
        print(key, value) # print each team out
    with open('result.json', 'w') as fp:
        json.dump(finalList, fp) # dump OrderedDict into JSON (for API purposes)
def skillsCheck(team, sku):
    skillList = [] # create list of skills scores from a tournament
    rawSkills = requests.get("https://api.vexdb.io/v1/get_skills?team=" + team + "&sku=" + sku) # get raw skills scores
    cleanSkills = rawSkills.json()

    for skill in cleanSkills['result']:
        skillList.append(skill['score'])
    try:
        maxSkill = max(skillList) # get highest skills score for a team at a comp
    except ValueError:
        maxSkill = 0
    return maxSkill
def awardsCheck(team, season):
    awardTotal = 0 # create award total
    rawAwards = requests.get("https://api.vexdb.io/v1/get_awards?team=" + team + "&season="+season) # get awards won by a team
    cleanAwards = rawAwards.json()
    if(cleanAwards['size'] == 0):
        awardTotal = 0
    for award in cleanAwards['result']: # add points to awardTotal based on award (+10 for champs, +8 for excel, +2 for judges, +3 for semis, +6 for everything else)
        rawStates = requests.get("https://api.vexdb.io/v1/get_events?sku="+award['sku']) # get type of award won by a team
        cleanstates = rawStates.json()
        if("Excellence" in award['name']):
            if("state" in cleanstates['result'][0]['name'].lower() or "regionals" in cleanstates['result'][0]['name'].lower() or "signature event" in cleanstates['result'][0]['name'].lower() or "nationals" or "regionals" in cleanstates['result'][0]['name'].lower()): #checks if award is from state/regional/sigevent/national comp
                awardTotal+=(8*1.5)

            else:
                awardTotal+=8

        elif("Champion" in award['name']):
            if("state" in cleanstates['result'][0]['name'].lower() or "regionals" in cleanstates['result'][0]['name'].lower() or "signature event" in cleanstates['result'][0]['name'].lower() or "nationals" or "regionals" in cleanstates['result'][0]['name'].lower()): #checks if award is from state/regional/sigevent/national comp
                awardTotal+=(10*1.5)

            else:
                awardTotal+=10

        elif("Judges" in award['name']):
            if("state" in cleanstates['result'][0]['name'].lower() or "regionals" in cleanstates['result'][0]['name'].lower() or "signature event" in cleanstates['result'][0]['name'].lower() or "nationals" or "regionals" in cleanstates['result'][0]['name'].lower()): #checks if award is from state/regional/sigevent/national comp
                awardTotal+=(2*1.5)

            else:
                awardTotal+=2

        elif("Semifinalists" in award['name']):
            if("state" in cleanstates['result'][0]['name'].lower() or "regionals" in cleanstates['result'][0]['name'].lower() or "signature event" in cleanstates['result'][0]['name'].lower() or "nationals" or "regionals" in cleanstates['result'][0]['name'].lower()): #checks if award is from state/regional/sigevent/national comp
                awardTotal+=(5*1.5)

            else:
                awardTotal+=5

        else:
            if("state" in cleanstates['result'][0]['name'].lower() or "regionals" in cleanstates['result'][0]['name'].lower() or "signature event" in cleanstates['result'][0]['name'].lower() or "nationals" or "regionals" in cleanstates['result'][0]['name'].lower()): #checks if award is from state/regional/sigevent/national comp
                awardTotal+=(4*1.5)

            else:
                awardTotal+=4
        return awardTotal
main()
