package com.twu.biblioteca;

import java.util.*;

public class BibliotecaApp {
    ArrayList<Book> bookList;
    ArrayList<Movie> movieList;
    ArrayList<User> userList;
    User user;

    public BibliotecaApp() {
        //showInterface();
        BookQuerier bookQuerier = new BookQuerier();
        this.bookList = bookQuerier.fetchBooksFromCSV();
        this.movieList = bookQuerier.fetchMoviesFromCSV();
        this.userList = bookQuerier.fetchUsersFromCSV();
    }

    public ArrayList<Book> getBookList(){
        return this.bookList;
    }

    public ArrayList<Movie> getMovieList(){
        return this.movieList;
    }

    public User getUser() {
        return user;
    }

    /* the user interface on console */
    public void showInterface(){
        System.out.println("Welcome to Biblioteca!");
        Scanner scanner = new Scanner(System.in);
        String input = "";
        String output = "";

        while(!input.equals("q")){
            System.out.print("[options]\tlist books (l)\tquit (q)\tcheck out a book (c)\treturn a book (r)" +
                    "\tlist movies (m)\tcheck out a movie (v)\tlog in (i)>> ");
            input = scanner.nextLine();
            if(!input.equals("l") && !input.equals("q") && !input.equals("c") && !input.equals("r")
                    && !input.equals("m") && !input.equals("v") && !input.equals("i")) {
                System.out.println("Select a valid option!");
                continue;
            }
            char opt = input.toCharArray()[0]; //should validate input later
            // statement of switch doesn't support String in jdk 8??
            switch(opt){
                case 'l':
                    System.out.println("List Books");
                    output = listBooks();
                    System.out.print(output);
                    break;
                case 'q':
                    break;
                case 'c':
                    System.out.print("Check Out A Book - please input its index >> ");
                    input = scanner.nextLine();
                    checkOutABook(input);
                    break;
                case 'r':
                    System.out.print("Return A Book - please input its name >> ");
                    input = scanner.nextLine();
                    returnABook(input);
                    break;
                case 'm':
                    System.out.println("List Movies");
                    output = listMovies();
                    System.out.print(output);
                    break;
                case 'v':
                    System.out.print("Check Out A Movie - please input its name >> ");
                    input = scanner.nextLine();
                    checkOutAMovie(input);
                    break;
                case 'i':
                    System.out.print("Log In - please input your librarian number and password, separate them with comma >> ");
                    input = scanner.nextLine();
                    logIn(input);
                    break;
            }

        }

        /*
        // why this loop becomes infinite after an irregular input?
        while(true){
            try{
                System.out.print("[options] list books (l) >> ");
                input = scanner.next("l");
                break;
            }
            catch(InputMismatchException e){
                System.out.println("Select a valid option!");
            }
        }
        */


        scanner.close();
    }

    /* list all books with index and name, print out on console */
    public String listBooks(){
        String output = "";

        for(Book book:this.bookList){
            output+=book.getIndex() + "\t" + book.getName() + "\t" + book.getAuthor() + "\t" + book.getYearPublished() + "\n";
        }
        return output;
    }

    public void checkOutABook(String index){
        if(this.user==null){
            System.out.println("Please log in first!");
            return;
        }

        boolean success = false;
        for(Book book: this.bookList){
            if(book.getIndex().equals(index)){
                this.bookList.remove(book);
                success = true;
                System.out.println("Thank you, "+this.user.getName()+"! Enjoy the book");
                break;
            }
        }
        if(!success){
            System.out.println("That book is not available.");
        }
    }

    public void returnABook(String name){
        if(this.user==null){
            System.out.println("Please log in first!");
            return;
        }

        BookQuerier bookQuerier = new BookQuerier();
        ArrayList<Book> allBooks = bookQuerier.fetchBooksFromCSV();
        boolean success = false;
        for(Book book: allBooks){
            if(book.getName().equals(name)){
                boolean contains = false;
                for(Book book1:bookList){
                    if(book1.getName().equals(book.getName())){
                        contains = true;
                        break;
                    }
                }
                if(!contains) {
                    this.bookList.add(book);
                    success = true;
                    System.out.println("Thank "+this.user.getName()+" for returning the book.");
                    break;
                }
            }
        }
        if(!success){
            System.out.println("That is not a valid book to return.");
        }
    }

    public String listMovies(){
        String output = "";

        for(Movie movie:this.movieList){
            output+=movie.getName() + "\t" + movie.getYear() + "\t" + movie.getDirector() + "\t" + movie.getRating() + "\n";
        }
        return output;
    }

    public void checkOutAMovie(String name){
        if(this.user==null){
            System.out.println("Please log in first!");
            return;
        }

        boolean success = false;
        for(Movie movie: this.movieList){
            if(movie.getName().equals(name)){
                this.movieList.remove(movie);
                success = true;
                System.out.println("Thank you, "+this.user.getName()+"! Enjoy the movie");
                break;
            }
        }
        if(!success){
            System.out.println("That movie is not available.");
        }
    }

    public void logIn(String input){
        String libraryNo = input.split(",")[0];
        String password = input.split(",")[1];

        for(User user:this.userList){
            if(user.getLibraryNo().equals(libraryNo)&&user.getPassword().equals(password)){
                this.user = user;
                break;
            }
        }
        if(this.user!=null){
            System.out.println("Welcome, "+user.getName()+"!\n"
                    +"librarian number: "+user.getLibraryNo()+"\n"
                    +"email: "+user.getEmail()+"\n"
                    +"phone: "+user.getPhoneNo());
        }
    }

    public static void main(String[] args) {
        BibliotecaApp bibliotecaApp = new BibliotecaApp();
        bibliotecaApp.showInterface();
    }
}
