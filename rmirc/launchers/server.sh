#!/bin/sh

java -Djava.security.policy=../policy/rmi.policy rmirc/Serveur/ServeurForum $@
