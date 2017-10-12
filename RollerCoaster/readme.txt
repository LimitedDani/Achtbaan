Documents

Thanks for using my Plugin!

Config

attracties: 
- example
//this is a list with all the attracties.

example:
//this is the attractie name.

  run_command: start
  //this is the run command.
  
  run_permissions: achtbaan.droomvlucht
  //this is the run command permission.
  
  spawn_command: spawn
  //this is the spawn command.
  
  spawn_permissions: achtbaan.droomvlucht
  //this is the spawn command permission.
  
  spawns:
  - 1
  - 2
  //this is a list with all the spawns
  
  spawn:
    1:
    - 'run:spawn world,0,73,0,0,0,diamond_hoe,2,0.3'
    - 'run:sit 0'
    2:
     - 'run:spawn world,0,73,0,0,0,diamond_hoe,2,0.3'
    - 'run:sit 1'
  	//list with the spawns it has to run.
  
  runs:
  - 1
  - 2
  //this is a list with all the runs
  
  run:
    1:
    - 'run:move 0.1,0,0.1,5,0'
    - 'run:move 0.1,0,0.1,5,0'
    - 'wait:1'
    - 'run:move 0.1,0,0.1,5,0'
    2:
    - 'run:move 0.1,0,0.1,5,0'
    - 'run:move 0.1,0,0.1,5,0'
    - 'wait:1'
    - 'run:move 0.1,0,0.1,5,0'
    //list with the void it has to run

End of the config.

Voids

(R) = relative
(O) = Optional

Every void has to start with the tag 'run'.
voids:
- eject x(R) y(R) z(R) yaw pitch
- move x(R) y(R) z(R) yaw(R) pitch(R) till(O)
- spawn world x(R) y(R) z(R) yaw(R) pitch(R) block blockdata radius(float) amount(int defualt 2)
- allow_set <true/false>
- allow_leave <true/false>
- remove true

Note! specials
- wait:<ticks>

End of the voids.

Commands

run: /achtbaan <attractie> <run_command(config)> <id> <run_id>
spawn: /achtbaan <attractie> <spawn_command(config)> <id> <spawn_id>

End of the Commands