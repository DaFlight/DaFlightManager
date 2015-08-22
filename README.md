DaFlightManager
===============
Server-side management of DaFlight users

=======

###About
DaFlightManager gives server owners fine-grain control over DaFlight-flymod users. It allows for features to be disabled/enabled, and even maximum fly/sprint speeds set, all defined by standard permission nodes. It also provides compatibility with the NoCheatPlus movement checks for Players using the mod - all other flight/speed-hack mods can still be blocked.<br>

###Download
http://build.dags.me/job/DaFlightManager-Bukkit/<br>
https://github.com/DaFlight/DaFlightManager/releases/

========

###Commands
- **/dfreload** - reloads the config file from disk
- **/dfrefresh <all, @target>** - re-sends the server controlled permissions to the client<br>
      (supply no arguments to refresh self, 'all' to refresh all online players, or specify the name of a specific player)

========

###Permissions
Nodes:
- **daflight.refresh** - allows use of the /dfrefresh command
- **daFlight.refresh.self** - allows user to refresh own daflight permissions
- **daflight.refresh.other** - allows user to refresh another user's daflight permissions
- **daflight.refresh.all** - allows user to refresh all online user's daflight permissions
- **daflight.reload** - allows use of the /dfreload command
- **daflight.fullbright** - allows use of the in-built fullbright feature
- **daflight.flymod** - allows use of the fly and sprint mod
- **daflight.noclip** - allows use of noclip whilst flying (in creative mode)
- **daflight.speed.#** - sets a maximum fly/sprint speed for the user<br>
      (Values of '#' can be defined via the plugin config)
