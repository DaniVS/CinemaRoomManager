package cinema;

import java.util.Scanner;

public class Cinema {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int rows = getRows(scanner);
        int seats = getSeats(scanner);
        int totalAvailability = rows * seats;
        int purchasedTickets = 0;
        int totalIncome = calculateIncome(rows, seats);
        int currentIncome = 0;
        int rowsToPrint = rows+1;
        int seatsToPrint = seats+1;
        char[][] cinema = initEmptyCinema(rowsToPrint, seatsToPrint);

        boolean exit = false;
        while (!exit){
            int userChoice = getMenuChoice(scanner);

            switch (userChoice){
                case 0:
                    exit = true;
                    break;
                case 1:
                    printCinema(cinema, rowsToPrint, seatsToPrint);
                    break;
                case 2:
                    int[] chosenSeat;

                    do {
                        chosenSeat = getUserChoice(scanner);
                    } while (!isSellable(chosenSeat, cinema));

                    printSeatPrice(cinema, chosenSeat);

                    currentIncome += calculateSeatPrice(cinema, chosenSeat);
                    purchasedTickets++;

                    updateSeatsAvailability(chosenSeat, cinema);

                    break;
                case 3:
                    printStatistics(totalAvailability, purchasedTickets, currentIncome, totalIncome);
                    break;
                default:
                    System.out.println("Wrong input!");
            }
        }
    }

    private static int getSeats(Scanner scanner) {
        System.out.println("Enter the number of seats in each row:");
        int seats = Integer.parseInt(scanner.nextLine());
        return seats;
    }

    private static int getRows(Scanner scanner) {
        System.out.println("Enter the number of rows:");
        int rows = Integer.parseInt(scanner.nextLine());
        return rows;
    }

    private static void initHeaders(char[][] cinema, int rowsToPrint, int seatsToPrint) {
        cinema[0][0] = ' ';

        initRowsHeader(cinema, rowsToPrint);
        initSeatsHeader(cinema, seatsToPrint);
    }

    private static void initRowsHeader(char[][] cinema, int rowsToPrint) {
        for (int r = 1; r < rowsToPrint; r++){
            cinema[r][0] = (char) (r + '0');
        }
    }

    private static void initSeatsHeader(char[][] cinema, int seatsToPrint) {
        for (int c = 1; c < seatsToPrint; c++){
            cinema[0][c] = (char) (c+'0');
        }
    }

    private static void printCinema(char[][] cinema, int rowsToPrint, int seatsToPrint) {
        System.out.println();
        System.out.println("Cinema:");
        for (int r = 0; r < rowsToPrint; r++){
            for (int c = 0; c < seatsToPrint; c++){
                System.out.print(cinema[r][c] + " ");
            }
            System.out.println();
        }
    }

    private static char[][] initEmptyCinema(int rowsToPrint, int seatsToPrint) {
        char[][] cinema = new char[rowsToPrint][seatsToPrint];
        initHeaders(cinema, rowsToPrint, seatsToPrint);
        initSeats(cinema, rowsToPrint, seatsToPrint);

        return cinema;
    }

    private static void initSeats(char[][] cinema, int rowsToPrint, int seatsToPrint) {
        for (int r = 1; r < rowsToPrint; r++){
            for (int c = 1; c < seatsToPrint; c++){
                cinema[r][c] = 'S';
            }
        }
    }

    private static int[] getUserChoice(Scanner scanner) {
        int[] seat = new int[2];

        System.out.println();
        System.out.println("Enter a row number:");
        seat[0] = scanner.nextInt();

        System.out.println("Enter a seat number in that row:");
        seat[1] = scanner.nextInt();

        return seat;
    }

    private static void printSeatPrice(char[][] cinema, int[] chosenSeat) {
        System.out.println();
        System.out.println("Ticket price: $" + calculateSeatPrice(cinema, chosenSeat));
    }

    private static int calculateSeatPrice(char[][] cinema, int[] chosenSeat) {
        int rows = cinema.length - 1;
        int seats = cinema[0].length - 1;
        int totalSeats = rows * seats;
        int fullPrice = 10;
        int reducedPrice = 8;
        int frontRows = rows / 2;

        if (totalSeats <= 60) {
            return fullPrice;
        }

        if (chosenSeat[0] <= frontRows) {
            return fullPrice;
        } else {
            return reducedPrice;
        }
    }

    private static int calculateIncome(int rows, int seats) {
        int totalSeats = rows * seats;
        int ticketPrice = 10;
        int reducedTicket = 8;
        int frontRows = rows / 2;
        int backRows = (rows % 2 > 0) ? (frontRows + 1) : frontRows;

        if (totalSeats <= 60) {
            return totalSeats * ticketPrice;
        } else {
            return seats * (
                    (frontRows * ticketPrice) + (backRows * reducedTicket)
            );
        }
    }

    private static void updateSeatsAvailability(int[] chosenSeat, char[][] cinema) {
        cinema[chosenSeat[0]][chosenSeat[1]] = 'B';
    }

    private static int getMenuChoice(Scanner scanner) {
        System.out.println();
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");

        int userChoice = scanner.nextInt();
        return userChoice;
    }

    private static boolean isSellable(int[] chosenSeat, char[][] cinema) {
        int theRow = chosenSeat[0];
        int theSeat = chosenSeat[1];

        if (theRow <= 0 || theSeat <= 0 || theRow > (cinema.length-1) || theSeat > (cinema[0].length-1)){
            System.out.println();
            System.out.println("Wrong input!");
            return false;
        }

        if(cinema[chosenSeat[0]][chosenSeat[1]] == 'B'){
            System.out.println("That ticket has already been purchased!");
            return false;
        }

        return true;
    }

    private static void printStatistics(int totalAvailability, int purchasedTickets, int currentIncome, int totalIncome) {
        System.out.println();
        System.out.println("Number of purchased tickets: " + purchasedTickets);
        System.out.printf("Percentage: %.2f%%%n", calculateSellingPercentage(totalAvailability, purchasedTickets));
        System.out.println("Current income: $" + currentIncome);
        System.out.println("Total income: $" + totalIncome);
    }

    private static float calculateSellingPercentage(int totalAvailability, int purchasedTickets) {
        return ((float) purchasedTickets / totalAvailability) * 100;
    }
}
