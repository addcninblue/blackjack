#Blackjack

##Class Design

###Card

#####Instance Variables:
- Suit
- Value
- Texture

####Methods:
- toString()

###GameDisplay
Displays the actual game

###Game
backend

#####Instance Variables:
- Card[52]
- List<Players>

####Methods:
- start()
- stop()
- renamePlayers()
- addPlayers()

###Players

#####Instance Variables:
- List<Cards>
- name
