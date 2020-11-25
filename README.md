# TPs de programmation réseaux

Veuillez trouver nos deux TPs de programmation réseaux dans les deux
sous-dossiers TP1 et TP2.

## TP1: Chats utilisant TCP et UDP/Multicast

Vous pouvez compiler avec `make` et lancer les `.class` depuis le répertoire
`bin`.

### UDP/Multicast

Un exemple très simple de communication UDP est disponible avec `java
stream.udp.UDPServer 2000` et `java stream.udp.UDPClient`.

La communication multicast est démontré avec `java stream.udp.UDPMulticast
224.0.0.1 4000` qui ne nécessite pas de serveur. Les utilisateurs sont ici
identifiés par un UUID et un nom qui figurent tous deux dans le datagram
packet avec le message. Example (point de vue de Ralph):

```
Choose a name:
Ralph
Hello!
<526a80e:Ralph> Hello!
<feg3dcd:Tom> Hey you
```

### TCP

Le serveur se lance avec: `java stream.tcp.EchoServerMultiThreaded 3004`

Le chat TCP est le plus développé. Les utilisateurs sont identifiés par leur
nom. Le serveur garde une hashmap des utilisateurs connectés (avec des UUIDs).
A chaque nouvelle connection d'utilisateur, le serveur créée un thread pour
s'en occuper (écouter et recevoir les messages). Le thread est interrompu une
fois l'utilisateur déconnecté (connexion interrompue). Le serveur reconnait deux
commandes:

```
!list - liste les utilisateurs connectés et la renvoie a l'utilisateur
!msg user msg - envoie msg à user (si ce dernier est connecté
```

Exemple:

```
<2020-11-25 08:15:23> Guillaume has joined the chatroom.
<2020-11-25 08:15:52> Lu has joined the chatroom.
<2020-11-25 08:16:09> Paul has joined the chatroom.
<2020-11-25 08:16:21> Paul: Coucou, ça se passe bien la démonstration ?
<2020-11-25 08:16:38> Lu: Hello everyone
<2020-11-25 08:17:04> Lu -> Guillaume: Private message right here!
<2020-11-25 08:18:51> Lu has left the chatroom.
```

L'utilisateur a le choix de se connecter via le terminal ou via une interface
graphique. Respectivement: `java stream.tcp.EchoClient localhost 3001` ou
`java stream.tcp.ChatWindow`.

Lors de sa connexion, le client inscrit son nom et le serveur lui retourne
ensuite la liste complète de l'historique.

## Serveur HTTP

Le serveur HTTP développé est visible via https://java.kessibi.fr.
C'est un serveur utilisant une pool de 20 threads, a chaque requete, un thread
renvoie le fichier demandé puis retourne dans le pool de threads disponibles.

Le serveur peut être compilé avec `make jar` puis `java -jar WebServer.jar
<port> <rootDir>` avec rootDir étant le dossier source de lecture des fichiers.
Par exemple, utilisez: `java -jar WebServer.jar 3000 www`.

Lors d'une connexion, le header de la requête est parsé, construit puis la
réponse est construite (et le fichier lu) dans `Response.java`

L'ensemble des élements HTTP supportés par le serveur est décrit dans l'index
disponible au choix à https://java.kessibi.fr ou http://localhost:3000. Il
comprend plusieurs codes de réponses (200, 500, 400, 451, ...), le `GET`, le
`POST`, les fichiers audio/vidéo, les fichiers dynamiques, ...


## Sur les TPs en général

Comme demandé, les documentations sont disponibles, dans `TP1/Code-Socket/doc`
et `TP2/TP-HTTP-Code/doc`.

Les deux TPS (serveur HTTP et chat TCP) sont dockerisés donc peuvent être
déployés de manière rapide et isolée. C'est notamment de cette manière que
le serveur HTTP est disponible sur https://java.kessibi.fr et qu'une instance
du chat est disponible sur kessibi.fr au port 3001.
