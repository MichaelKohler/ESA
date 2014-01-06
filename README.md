ESA - Bewegungsmelder
===

Projekt von Ralf Wittich, Mario Aloise, Michael Kohler


NetzwerkService
===

Standardmässig wird alle 5 Minuten ein Ping an einen Server gesendet, um zu überprüfen, ob die App noch läuft. So kann ein aussenstehender Kontakt überprüfen, ob alles läuft.

Das Interval, der Server und der dazugehörige Port kann in den App-Einstellungen eingestellt werden.

Standard-Werte:
Server: michaelkohler.info
Port: 3001
Interval: 5

Der Status kann unter `http://michaelkohler.info:3001/status/5` abgerufen werden, wobei 5 das in der App eingestellte Interval ist.

Den serverseitigen Code steht unter `https://github.com/MichaelKohler/PingWithPOST` zur Verfügung (mit Installations-Anleitung).

Momentan aktualisiert sich die Status-Seite nicht automatisch und muss daher manuell neu geladen werden.
