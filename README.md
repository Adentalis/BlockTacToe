# BlockTacToe
A Ethereum-Smartcontract to play TicTacToe 

The new one for v5 
changelogs
- remove fallback
- remove Patricks code
- remove getStartingPlayer by rnd
- safe some variables as memory 

pragma solidity >=0.4.21 <0.7.0;

contract BlockTacToe {
    event NewGame(address player1 , address player2, uint gameId);
    
    struct Game{
        uint id;
        uint bet;
        address player1;
        address player2;
        uint turn;
        uint[] gameHistory;
        uint[] board;
        GameState gameState;
        uint lastTurnMade;
    }
    
    enum GameState{
        RUNNING,TIE,PLAYER1WINS, PLAYER2WINS , PLAYER1TIMESUP , PLAYER2TIMESUP
    }
    
    //Array where all games will be stored  
    Game[] public games;
    uint public timeForOneMove = 10 minutes;
    
    //get the gameId from an address
    mapping(address => uint)public  playerGameId;
    
    //return the balance of an address
    mapping(address =>uint )public balances;
    
    //player 1 is doing a gameRequest to player 2 with the amount to play
    mapping(address => mapping (address =>uint)) public gameRequest;
    
    //player 1 -> player 2
    mapping(address => address) public wantsToPlayWith;
    
    //palyer 2 -> player 1
    mapping(address => address) public hasToAcceptTheGame;

    
    function deposit()public payable{
    	require(msg.value>0);
        balances[msg.sender]+=msg.value;
    }
    
    function withdraw() public{
        //not allowed during an active game
        require(balances[msg.sender]>0);
        msg.sender.transfer(balances[msg.sender]);
        balances[msg.sender]=0;
    }
    
    //player 1 is doing a gameRequest to player 2
    function initGame(address _opponent, uint _gameValue)public {
        
        //player 1 must have more value on balances
        require(balances[msg.sender]>=_gameValue);
        //player 1 isn't allowed to be in another Game
        require(playerGameId[msg.sender]==0);
        //Player 1 has no other gameRequest
        require(wantsToPlayWith[msg.sender]==address(0));
        
        gameRequest[_opponent][msg.sender]=_gameValue;
        wantsToPlayWith[msg.sender]=_opponent;
        hasToAcceptTheGame[_opponent]=msg.sender;
        
    }
    
    //Player 1 revoke his gameRequest
    function revokeGameRequest()public {
        
        require(wantsToPlayWith[msg.sender]!=address(0));
        address opponent = wantsToPlayWith[msg.sender];
        gameRequest[opponent][msg.sender]=0;
        wantsToPlayWith[msg.sender]=address(0);
        hasToAcceptTheGame[opponent]=address(0);
        
    }
    
    // player 2 DONT accept the gameRequest
    
    function denyGameRequest()public{
        require(hasToAcceptTheGame[msg.sender]!=address(0));
        address opponent = hasToAcceptTheGame[msg.sender];
        //reset the mappings
        gameRequest[msg.sender][opponent]=0;
        hasToAcceptTheGame[msg.sender]=address(0);
        wantsToPlayWith[opponent]=address(0);
        
    }
    
    //player 2 accepts the gameRequest
    function acceptGameRequest() public{

        require(hasToAcceptTheGame[msg.sender]!=address(0));
        
        //check if player 2 has enough balbalances
        address opponent = hasToAcceptTheGame[msg.sender];
        require(balances[msg.sender] >= gameRequest[msg.sender][opponent]);
        require(playerGameId[msg.sender]==0);
        require(wantsToPlayWith[msg.sender]== address(0));
        
        //The players balances will be added in the games bet
        balances[msg.sender]-=gameRequest[msg.sender][opponent];
        balances[opponent]-=gameRequest[msg.sender][opponent];
        
        //reset the mappings
        gameRequest[msg.sender][opponent]=0;
        hasToAcceptTheGame[msg.sender]=address(0);
        wantsToPlayWith[opponent]=address(0);
        
        createGame(msg.sender,opponent, gameRequest[msg.sender][opponent]*2);
        
    }
    

    function createGame(address _p1, address _p2, uint _bet) private{
        //none of the addresses is currently in a running game
        require(playerGameId[_p1] == 0 &&playerGameId[_p2] == 0);
        
        uint _newGameId = games.length+1;
        
        bool isPlayer1Starting = getRandomStartingPlayer();
        
        // create a new game
        if(isPlayer1Starting == true)
        games.push(Game( _newGameId,_bet, _p1, _p2,1,new uint[](0),new uint[](9),GameState.RUNNING,now ));
        else
        games.push(Game( _newGameId,_bet, _p2, _p1,1,new uint[](0),new uint[](9),GameState.RUNNING,now ));

        playerGameId[_p1]=_newGameId;
        playerGameId[_p2]=_newGameId;
        
        emit NewGame(_p1,_p2, _newGameId);
    }
    
    function doTurn(uint _pos) public{
        //address is in a running game
        require(playerGameId[msg.sender]!=0);
        
        //creates a storage game to work on
        Game storage game =games[playerGameId[msg.sender]-1];
        require(checkAllowedTurns(game, _pos) == true);
        
        //Player 1 is just allowed to do a turn when turns == odd ; Player 2 when turns == even
        require(game.turn%2==1 && msg.sender==game.player1 || game.turn% 2 == 0 && msg.sender==game.player2);

        game.gameHistory.push(_pos);
        game.lastTurnMade = now;
            
        //fill the board
        if(game.player1 == msg.sender){
            game.board[_pos-1]=1;
        }else{
            game.board[_pos-1]=2;
        }
       
        if(game.turn >= 5)
        game.gameState = checkWinner(game);
        
        if(game.gameState != GameState.RUNNING)
        aftergame(game);
        
        game.turn ++;
    }
    
    function aftergame(Game memory _game) private{
       	playerGameId[_game.player1]=0;
        playerGameId[_game.player2]=0;
        
        // return the gamebet to the players balances
        if(_game.gameState == GameState.TIE){
            balances[_game.player1]+= _game.bet/2;
            balances[_game.player2]+= _game.bet/2;
        }else{
             if(_game.gameState == GameState.PLAYER1WINS ||_game.gameState == GameState.PLAYER2TIMESUP ){
                balances[_game.player1]+= _game.bet;
            }else{
                balances[_game.player2]+= _game.bet;
            }
        }
    }
    
    function killGame(uint _id) public{
        games[_id-1].gameState = GameState.TIE;
        aftergame(games[_id-1]);
    }
    
    function getOpponent() public view returns(address){
        uint gameId = getGameid(msg.sender)-1;
        if(games[gameId].player1 == msg.sender){
            return games[gameId].player2;
        }else{
            return games[gameId].player1;
        }
    }
    
    function checkTimeForOneTurnOver(uint _id) public{
         require(playerGameId[msg.sender]!=0);
         
         address activePlayer;
         
         if(games[_id-1].turn % 2 == 1 )
         activePlayer = games[_id-1].player1;
         else
         activePlayer = games[_id-1].player2;
         
         require(activePlayer != msg.sender);
         
         bool timeOver = false;
         if(now-games[_id-1].lastTurnMade > timeForOneMove ){
             timeOver = true;
         }
         
         if(timeOver){
            if(activePlayer==games[_id-1].player1){
                games[_id-1].gameState = GameState.PLAYER1TIMESUP;
            }else{
                games[_id-1].gameState = GameState.PLAYER2TIMESUP;
            }
         }
        
        //if the game is over call the aftergame function
        if(games[_id-1].gameState != GameState.RUNNING){
            aftergame(games[_id-1]);
        }
         
    }
    
    
   function checkWinner(Game memory _game) pure private returns (GameState)  {
        uint toCheck;
        bool gameWon = false;
        
        //if turn == 5/7/9 check player 1
        if(_game.turn%2 == 1){
            toCheck=1;
        }else{
            toCheck=2;
        }
        
        //checks all the 8 possible winning possibilities in a TicTacToe game
        if( (_game.board[0]==toCheck && _game.board[4]==toCheck && _game.board[8]==toCheck)||
            (_game.board[2]==toCheck && _game.board[4]==toCheck && _game.board[6]==toCheck)||
            (_game.board[0]==toCheck && _game.board[1]==toCheck && _game.board[2]==toCheck)||
            (_game.board[3]==toCheck && _game.board[4]==toCheck && _game.board[5]==toCheck)||
            (_game.board[6]==toCheck && _game.board[7]==toCheck && _game.board[8]==toCheck)||
            (_game.board[0]==toCheck && _game.board[3]==toCheck && _game.board[6]==toCheck)||
            (_game.board[1]==toCheck && _game.board[4]==toCheck && _game.board[7]==toCheck)||
            (_game.board[2]==toCheck && _game.board[5]==toCheck && _game.board[8]==toCheck))
            gameWon = true;
        
        //if there is a winner change the gameState of the game
        if(gameWon == true){
            if(toCheck == 1)
            return GameState.PLAYER1WINS;
            else
            return GameState.PLAYER2WINS;
        }
        
        //After the 9. turn is done and there is no winner yet the game ends in a tie 
        if(_game.turn == 9 && gameWon == false)
            return GameState.TIE;
   }
  
    
    
    function checkAllowedTurns(Game  memory _game, uint _pos) private pure returns(bool){
        
        if(_game.board[_pos-1] == 0 && _pos <= 9 && _pos >=1)
        return true;
        
        return false;
    }
    
   
    function getRandomStartingPlayer() private view returns(bool){
        
       uint8 result = 1;
        if (result == 0 )
            return true;
        else
            return false;
        
    }
    
    /*
    Helperfuncions
    */
    
    function getGameid(address _player) public view returns(uint){
        return playerGameId[_player];
    }
    
    function getStartingPlayer(uint _id) public view returns(address){
        return games[_id-1].player1;
    }
    
    function getGameState(uint _id) public view returns(GameState){
        return games[_id-1].gameState;
    }
    
    function getTurn(uint _id) public view returns(uint){
       return games[_id-1].turn;
    }
    
    function getBet(uint _id) public view returns(uint){
       return games[_id-1].bet;
    }
    
    function getGameHistory(uint _id) public view returns(uint[] memory){
        return games[_id-1].gameHistory;
    }
    
    function getTimeSinceLastTurn(uint _id) public view returns(uint){
        return games[_id-1].lastTurnMade;
    }
   
    function getBoard(uint _id) public view returns(uint[] memory){
        return games[_id-1].board;
    }
   
}
