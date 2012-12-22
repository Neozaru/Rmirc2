#!/bin/sh

java -Djava.security.policy=../policy/rmi.policy rmirc/Fournisseur/FournisseurDeSujet $@
