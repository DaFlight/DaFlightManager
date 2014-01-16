DaFlightManager
===============
Server-side management of DaFlight users

###Download
http://dl.dags.me/Plugins/DaFlightManager.jar

=======

###About
DaFlightManager gives server owners fine-grain control over DaFlight-flymod users. It allows for features to be disabled/enabled, and even maximum fly/sprint speeds set, all defined by standard permission nodes. It also provides compatibility with the NoCheatPlus movement checks for Players using the mod - all other flight/speed-hack mods can still be blocked.<br/>

========

###Permissions
Nodes:
- **daflight.fullbright** - allows use of the in-built fullbright feature
- **daflight.flymod** - allows use of the fly and sprint mod
- **daflight.speed.#** - sets a maximum fly/sprint speed for the user.<br/> 
      (Values of '#' can be [2, 5, 7, 10, 15, 25, 50])

Group Nodes:
- **daflight.user** - fullBright = true, flyMod = true, maxSpeed = 2
- **daflight.mod** - fullBright = true, flyMod = true, maxSpeed = 7
- **daflight.admin** - fullBright = true, flyMod = true, maxSpeed = 15
- **daflight.*** - fullBright = true, flyMod = true, maxSpeed = 50 (un-limited)
