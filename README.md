# Server Renicer

## About
This is a small utility for servers have full access to their machine, want to _renice_ their servers but for whatever reason can't automate the process.
We **do not** encourage the use of this project, it is advisable to modify your script to _renice_ the process in the startup script or modify Pterodactyl to reliable find the PID to _renice_.

## Requirements

* `sudo` access for Unix's `renice` command

## Considerations
The purpose of this project is **prevent** executing your server like this:
```sh
sudo nice -10 screen -AmdS server java -jar server.jar
```

Nice priorities below 0 **require** sudo access, executing `nice` will make the whole server as root, which represents a high security risk. Using `renice` instead, once the server has started will avoid executing the whole server as root.\
This project is equivalent to executing the following [command](https://unix.stackexchange.com/questions/294299/how-to-renice-all-threads-and-children-of-one-process-on-linux) once the server has started:
```sh
ls /proc/$PID/task | sudo xargs renice -n -10
```

As an additional measure, **only** the `renice` command should be allowed to execute as sudo by the user. \
It is worth remembering that allowing sudo execution without password should always be limited to mandatory commands and additional countermeasures should always be welcomed. \
This is an example of a `sudo` rule limited only to `renice` command in `/etc/sudoers` file:
```sudo
user ALL=(ALL) NOPASSWD: /usr/bin/renice
```
