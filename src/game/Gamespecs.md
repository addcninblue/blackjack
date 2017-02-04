#Specs

from main

  hasPlayers
  - @param none
  - @returns boolean: whether or not there are still players in the serverGame

  newRound
  - @param none
  - @return none
  - starts a new round of a serverGame (for initialization purposes). Execute resetTurn() in here.

  endRound
  - @param none
  - @return none
  - ends the current round of a serverGame

from setBets:

  getPlayers
  - @param none
  - @return list of players

  setBet
  - @param Person person
  - @param int bet
  - @return boolean: true on success, false on failure
  - sets the bet and returns true if bet is less than player money
  - returns false if bet is greater than player money

from deal:

  deal
  - @param none
  - @return none
  - deals cards to everyone (updates their hand)
  player.getHand()
  - @param none
  - @return player's hand (Arraylist of cards)

from playerTurns:

  hit
  - @param, @return none
  - adds a card to a person, and demotes aces if it's too large

from dealerTurn:

  dealCardToDealer
  - @param none
  - @return card dealt
  - deals card to dealer

from payBets:

  payBets:
  - @param player
  - @return none
  - pays money to player

from removeMoneyLess:

  removeMoneyLess:
  - @param none
  - @return the players who were removed
