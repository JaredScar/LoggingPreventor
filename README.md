# LoggingPreventor

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

[LoggingPreventor Bukkit Plugin Page] (https://dev.bukkit.org/projects/loggingpreventor)

LoggingPreventor was a plugin created to prevent those players who log out to avoid pvp or contact with players if they feel they are losing or going to lose. When you get hit by a player, you both are added to a combat tag. This combat tag expires if you do not fight in the amount of time defined in the config. If you log in while you are combat tagged, then it will spawn a villager in your place. Another player can then kill this villager and it will kill you if they killed it.

<h2>Permissions</h2>
None yet.

<h2>Commands</h2>
None yet.

<h2>Config</h2>
```YAML
CombatTagExpireTime: 30.0 # How long until the combat tag should expire. (Seconds)
Messages:
  PlayerKilledOnLogin: '&e%killed% &clogger was killed and he came back to a dead &e%killed%!'
  LoggerWasKilled: '&e%killed% &clogger was killed by &4%killer%&c!'
```  
