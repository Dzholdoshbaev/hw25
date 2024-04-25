import enums.ActionLetter;
import exceptions.UserInputExecption;
import model.*;
import paymantMethod.CardAcceptor;
import paymantMethod.CoinAcceptor;
import util.UniversalArray;
import util.UniversalArrayImpl;
import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final CoinAcceptor coinAcceptor;
    private final CardAcceptor cardAcceptor;
    private Scanner scanner = new Scanner(System.in);

    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        coinAcceptor = new CoinAcceptor(100);
        cardAcceptor = new CardAcceptor(0);
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);
        print("Выберите способ оплаты : ");
        print("1) Монетами 2) Картой ");
        int choosenumber = askPaymantChoice();
        if (choosenumber == 1){
            print("Монет на сумму: " + coinAcceptor.getAmount());

            UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
            allowProducts.addAll(getAllowedProducts().toArray());
            chooseAction(allowProducts);
        }if (choosenumber == 2){
            payWithCard();
            print("Ваш баланс после пополнения : " + cardAcceptor.getAmount());
        }
    }
    private void payWithCard(){
       askCard();
       chooseNumberOfFillMachine();
    }
    private void chooseNumberOfFillMachine(){
        try{
            print("Введите сумму пополнения до 130 ");
            int chosenNumber = Integer.parseInt(scanner.nextLine().trim());
            if (chosenNumber <= 0 ){
                throw new UserInputExecption();
            }if (chosenNumber > 130){
                throw new UserInputExecption();
            }
            cardAcceptor.setAmount(cardAcceptor.getAmount() + chosenNumber);
        }catch (NumberFormatException e){
            print("Надо ввести число !");
            chooseNumberOfFillMachine();
        }catch (NullPointerException e){
            print("Вы не ввели ничего !");
            chooseNumberOfFillMachine();
        }catch (UserInputExecption e){
            print("Число не может быть 0 и больше 130 и быть со знаком - !");
            chooseNumberOfFillMachine();
        }
    }
    private void askCard(){
        try{
            print("Введите номер карты : ");
            String input = scanner.nextLine().trim();
            int numberOfCard = Integer.parseInt(input);
            if (numberOfCard < 0 ){
                throw new UserInputExecption();
            }if (input.length() > 8 ){
                throw new UserInputExecption();
            }
        }catch (NumberFormatException e){
            print("Надо ввести число !");
            askCard();
        }catch (NullPointerException e){
            print("Вы не ввели ничего !");
            askCard();
        }catch (UserInputExecption e){
            print("Номер карты не может быть больше 8 цифр и быть со знаком - !");
            askCard();
        }
        try{
            print("Введите одноразовый пароль : ");
            String inputPassword = scanner.nextLine().trim();
            int numberOfPassword = Integer.parseInt(inputPassword);
            if (numberOfPassword < 0 ){
                throw new UserInputExecption();
            }if (inputPassword.length() > 4 ){
                throw new UserInputExecption();
            }
        }catch (NumberFormatException e){
            print("Надо ввести число !");
            askCard();
        }catch (NullPointerException e){
            print("Вы не ввели ничего !");
            askCard();
        }catch (UserInputExecption e){
            print("Пароль не может быть больше 4 цифр и быть со знаком - !");
            askCard();
        }
    }
    private int askPaymantChoice(){
        try{
            int chosenNumber = Integer.parseInt(scanner.nextLine().trim());
            if (chosenNumber <= 0 ){
                throw new UserInputExecption();
            }if (chosenNumber > 2){
                throw new UserInputExecption();
            }
            return chosenNumber;
        }catch (NumberFormatException e){
            print("Надо ввести число !");
            return askPaymantChoice();
        }catch (NullPointerException e){
            print("Вы не ввели ничего !");
            return askPaymantChoice();
        }catch (UserInputExecption e){
            print("Число не может быть 0 и больше 2 и быть со знаком - !");
            return askPaymantChoice();
        }
    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        print(" a - Пополнить баланс");
        showActions(products);
        print(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        if ("a".equalsIgnoreCase(action)) {
            coinAcceptor.setAmount(coinAcceptor.getAmount() + 10);
            print("Вы пополнили баланс на 10");
            return;
        }
        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    coinAcceptor.setAmount(coinAcceptor.getAmount() - products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName());
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            if ("h".equalsIgnoreCase(action)) {
                isExit = true;
            } else {
                print("Недопустимая буква. Попрбуйте еще раз.");
                chooseAction(products);
            }
        }


    }

    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
