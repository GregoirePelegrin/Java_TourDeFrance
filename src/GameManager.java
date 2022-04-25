import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class GameManager {
    public boolean gameFinished;
    public int winner;
    private final ArrayList<Boolean> board;
    private final ArrayList<Card> deck;
    private final ArrayList<Player> players;
    private final ArrayList<String> colors;
    private final ArrayList<String> figures;
    private boolean acedTurn;
    private boolean stageFinished;
    private int stage;
    private int turn;

    public GameManager(){
        // Initialize
        this.gameFinished = false;
        this.winner = 0;
        this.board = new ArrayList<>();
        this.deck = new ArrayList<>();
        this.players = new ArrayList<>();
        this.colors = new ArrayList<>(Arrays.asList(
                "spades",
                "hearts",
                "clubs",
                "diamonds"
        ));
        this.figures = new ArrayList<>(Arrays.asList(
                "Ace",
                "King",
                "Queen",
                "Jack",
                "10",
                "9",
                "8",
                "7",
                "6",
                "5",
                "4",
                "3",
                "2"
        ));
        this.stage = 0;
        this.turn = 0;
        this.acedTurn = false;

        // Generate players
        Scanner scanner = new Scanner(System.in);
        System.out.print("Number of players: ");
        int nbPlayers = scanner.nextInt();
        for(int i=0; i<nbPlayers; i++){
            this.players.add(new Player());
        }
        // Generate deck
        int toDealCards = Math.min(52, 10*this.players.size());
        for(int i=0; i<(toDealCards-toDealCards%4)/4; i++){
            for(String col : colors){
                this.deck.add(new Card(col, figures.get(i)));
                this.board.add(false);
            }
        }
        if(toDealCards % 4 != 0){
            this.deck.add(new Card("spades", figures.get((toDealCards-toDealCards%4)/4)));
            this.deck.add(new Card("clubs", figures.get((toDealCards-toDealCards%4)/4)));
            for(int i=0; i<4; i++) this.board.add(false);
        }
    }

    public void nextStage(){
        this.stageFinished = false;

        for(int i=0; i<this.board.size(); i++) this.board.set(i, false);

        ArrayList<Card> copyDeck = new ArrayList<>(this.deck);
        Collections.shuffle(copyDeck);

        // Deal cards
        int dealedCards = 0;
        while(!copyDeck.isEmpty()) {
            dealedCards++;
            int receivingPlayer = dealedCards % this.players.size();
            Card currCard = copyDeck.remove(0);
            this.players.get(receivingPlayer).addCard(currCard);
        }

        switch(this.stage%3){
            case 0:
                System.out.println("Normal, 1 card at a time (excepted when playing an ace)");
                break;
            case 1:
                System.out.println("Mountain, everything in the same color (excepted when playing an ace)");
                break;
            case 2:
                System.out.println("Chrono, everything everywhere");
                break;
        }
        this.showBoard();

        while(!this.stageFinished){
            if(this.stage%3 == 0) this.nextTurnNormal();
            else if(this.stage%3 == 1) this.nextTurnMountain();
            else this.nextTurnChrono();
        }
        for(int i=0; i<this.players.size(); i++){
            Player player = this.players.get(i);
            player.calculateScore();
            player.flushHand();
            if(player.getMinutes() < this.players.get(i).getMinutes() || (player.getMinutes() < this.players.get(i).getMinutes() && player.getSeconds() < this.players.get(i).getSeconds()))
                this.winner = i;
        }
        this.stage++;

        Scanner scanner = new Scanner(System.in);
        System.out.println(this.getScores());
        System.out.print("Do you want to stop now? (y/n): ");
        if(scanner.next().equalsIgnoreCase("y")) this.gameFinished = true;
    }
    public String getScores(){
        StringBuilder scores = new StringBuilder();
        for(int i=0; i<this.players.size(); i++){
            Player player = this.players.get(i);
            scores.append("Player ").append(i + 1).append(": ").append(player.getScore());
            if(i != this.players.size()-1) scores.append("\n");
        }
        return scores.toString();
    }

    private void nextTurnChrono(){
        Player currPlayer = this.players.get(this.turn%this.players.size());
        ArrayList<Card> playedCards = this.getCardMultiple(currPlayer, true, this.turn%this.players.size());
        while(!this.arePlayableChrono(currPlayer, playedCards)) playedCards = this.getCardMultiple(currPlayer, false, this.turn%this.players.size());
        currPlayer.playCards(playedCards);
        if(playedCards != null){
            for(Card playedCard : playedCards) this.board.set(playedCard.getPosition(), true);
            if(currPlayer.getNbCards() == 0){
                this.stageFinished = true;
            }
            Collections.sort(playedCards);
            StringBuilder displayCards = new StringBuilder();
            for(Card card : playedCards) displayCards.append(card).append(", ");
            System.out.println(displayCards);
        } else {
            System.out.println("Pass");
        }
        this.showBoard();
        this.turn++;
    }
    private void nextTurnMountain(){
        Player currPlayer = this.players.get(this.turn%this.players.size());
        ArrayList<Card> playedCards = this.getCardMultiple(currPlayer, true, this.turn%this.players.size());
        while(!this.arePlayableMountain(currPlayer, playedCards, this.acedTurn)) playedCards = this.getCardMultiple(currPlayer, false, this.turn%this.players.size());
        this.acedTurn = false;
        currPlayer.playCards(playedCards);
        if(playedCards != null){
            for(Card playedCard : playedCards) this.board.set(playedCard.getPosition(), true);
            if(currPlayer.getNbCards() == 0){
                this.stageFinished = true;
            }
            Collections.sort(playedCards);
            StringBuilder displayCards = new StringBuilder();
            for(Card card : playedCards) displayCards.append(card).append(", ");
            System.out.println(displayCards);
        } else {
            System.out.println("Pass");
        }
        this.showBoard();
        if(playedCards == null || !playedCards.get(playedCards.size()-1).getFigure().equalsIgnoreCase("ace")) this.turn++;
        else {
            this.acedTurn = true;
            System.out.println("Your turn again, remember you can pass this one");
        }
    }
    private void nextTurnNormal(){
        Player currPlayer = this.players.get(this.turn%this.players.size());
        Card playedCard = this.getCardNormal(currPlayer, true, this.turn%this.players.size());
        while(!this.isPlayable(currPlayer, playedCard, this.board, this.acedTurn)) playedCard = this.getCardNormal(currPlayer, false, this.turn%this.players.size());
        this.acedTurn = false;
        currPlayer.playCard(playedCard);
        if(playedCard != null){
            this.board.set(playedCard.getPosition(), true);
            if(currPlayer.getNbCards() == 0){
                this.stageFinished = true;
            }
            System.out.println(playedCard);
        } else {
            System.out.println("Pass");
        }
        this.showBoard();
        if(playedCard == null || !playedCard.getFigure().equalsIgnoreCase("ace")) this.turn++;
        else {
            this.acedTurn = true;
            System.out.println("Your turn again, remember you can pass this one");
        }
    }
    private ArrayList<Card> getCardMultiple(Player player, boolean isFirstTime, int id){
        if(!isFirstTime){
            System.out.println("Faulty move, a penalty will be added to your score");
            player.addPenalty();
        }
        player.showHand(id);
        Scanner scanner = new Scanner(System.in);
        System.out.print("What card(s) to play (separated by comas, if needed): ");
        String cardsList = scanner.nextLine();
        cardsList = cardsList.replaceAll("\\s", "");
        String[] cardsListSplitted = cardsList.split(",");
        ArrayList<Card> cardsToPlay = new ArrayList<>();
        for(String indexCard : cardsListSplitted){
            try{
                int index = Integer.parseInt(indexCard)-1;
                cardsToPlay.add(player.getCard(index));
            } catch (Exception e) {
                return null;
            }
        }
        return cardsToPlay;
    }
    private Card getCardNormal(Player player, boolean isFirstTime, int id){
        if(!isFirstTime) {
            System.out.println("Faulty move, a penalty will be added to your score");
            player.addPenalty();
        }
        player.showHand(id);
        Scanner scanner = new Scanner(System.in);
        System.out.print("What card to play: ");
        int indexCard;
        try{
            indexCard = scanner.nextInt()-1;
        } catch(Exception e) {
            return null;
        }
        while(indexCard >= player.getNbCards()){
            System.out.print("This card does not exist, what card to play (index+1): ");
            indexCard = scanner.nextInt()-1;
        }
        if(indexCard == -1) return null;
        return(player.getCard(indexCard));
    }
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean arePlayableChrono(Player player, ArrayList<Card> cards){
        if(cards == null){
            for(int i=0; i<player.getNbCards(); i++){
                if(this.isPlayable(player, player.getCard(i), this.board, false)) return false;
            }
            return true;
        }
        ArrayList<Card> copyCards = new ArrayList<>(cards);
        ArrayList<Boolean> copyBoard = new ArrayList<>(this.board);
        boolean result = true;
        while(result && !copyCards.isEmpty()){
            result = false;
            for(Card card : copyCards){
                if(this.isPlayable(player, card, copyBoard, false)){
                    copyBoard.set(card.getPosition(), true);
                    result = true;
                    copyCards.remove(card);
                    break;
                }
            }
        }
        return result;
    }
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean arePlayableMountain(Player player, ArrayList<Card> cards, boolean canPass){
        if(cards == null){
            if(canPass) return true;
            for(int i=0; i<player.getNbCards(); i++){
                if(this.isPlayable(player, player.getCard(i), this.board, false)) return false;
            }
            return true;
        }
        ArrayList<Card> copyCards = new ArrayList<>(cards);
        ArrayList<Boolean> copyBoard = new ArrayList<>(this.board);
        boolean result = true;
        while(result && !copyCards.isEmpty()){
            result = false;
            for(Card card : copyCards){
                if(this.isPlayable(player, card, copyBoard, false)){
                    copyBoard.set(card.getPosition(), true);
                    result = true;
                    copyCards.remove(card);
                    break;
                }
            }
        }
        if(result){
            String currCol = cards.get(0).getColor();
            for(Card card : cards){
                if(!card.getColor().equals(currCol)) return false;
            }
        }
        return result;
    }
    private boolean isPlayable(Player player, Card card, ArrayList<Boolean> tempBoard, boolean canPass){
        if(card == null){
            if(canPass) return true;
            for(int i=0; i<player.getNbCards(); i++){
                if(this.isPlayable(player, player.getCard(i), tempBoard, false)) return false;
            }
            return true;
        }
        int x = -1;
        int y = -1;
        int direction;
        for(int i=0; i<this.colors.size(); i++){
            if(card.getColor().equals(this.colors.get(i))) x = i;
        }
        for(int i=0; i<this.figures.size(); i++){
            if(card.getFigure().equals(this.figures.get(i))) y = i;
        }
        int currY = 4;
        if(y == currY) return true;
        if(y > currY) direction = 1;
        else direction = -1;
        while(currY != y && tempBoard.get(currY*4+x)){
            currY += direction;
        }
        return currY == y;
    }
    private void showBoard(){
        StringBuilder result = new StringBuilder();
        for(int i=0; i<this.board.size(); i++){
            result.append("| ");
            if(this.board.get(i)) {
                String color = this.colors.get(i % 4);
                String figure = this.figures.get((i - i % 4) / 4);
                Card currCard = new Card(color, figure);
                result.append(currCard.shortString());
                if(!figure.equals("10")) result.append(" ");
            } else {
                result.append("   ");
            }
            result.append(" |");
            if(i%4 == 3) result.append("\n");
            else result.append(" ");
        }
        System.out.print(result);
    }
}
