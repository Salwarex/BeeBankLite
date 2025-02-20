# Description
BeeBankLite - a Spigot plugin that allows you to implement a simple in-game banking system on a server using customizable items that players can mine in the world as they survive. It was originally created by an administrator for a now-closed vanilla server.
# Installation
To install, you need to place the file in the plugins folder, start and disable the server, and configure all the necessary configuration (database and currency) in *...\BeeBankLite\config.yml*. After configuring the configuration, you can start the server again.
# Using
- **/bank** - open the banking interface. It allows you to view the amount of the available currency, the ability to switch the currency (if you have entered several currencies in the config), replenish the balance or withdraw funds.
  - **Withdraw** - It occurs by issuing funds to the inventory and simultaneously withdrawing funds from the account with the player's nickname from the database.
  - **Deposite** - requires that the player's right hand contains one of the currencies specified in config.yml. If the condition is met, the funds will be withdrawn from the hand and credited to the account in the database.
  - **/bank history {nickname}** - shows the history of operations on the specified account.
- **/pay {sum} {nickname}** - sends funds to the specified player's account, if it exists.
##Permissions
- **bbl.history.others** - view the transaction history of other players.
- **bbl.reload** - reload the config.
# Supported techonologies
- SQLite/MySQL database
- PlaceholderAPI (beebanklite:mainbalance)
- English/Russian language
