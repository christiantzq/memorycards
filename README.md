# Memory Cards

Code for the course of Design Patterns with Mosh

Setup Spring-boot 2.7.17 and Java 11

Setup with `mvn spring-boot:run`

## Required Env Vars
- Edit file `sudo vim ~/.profile`
- Add this to the end `export DOC_PATH="/home/<myUser>/doc/"`
- Log out and re-log in.
- Test with `echo $MY_VAR`

## Other Setup

- Ubuntu server Installation directory:
  - `/opt/memorycards/memorycards-0.1.jar`

- Service:
  - `/etc/systemd/system/memorycards.service`

- Shell
  - `/usr/local/bin/memorycards.sh`