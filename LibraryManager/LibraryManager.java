package exam_24_12.LibraryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Book {
    private String title;
    private String author;
    private String isbn;
    private int year;

    public Book(String title, String author, String isbn, int year) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getYear() {
        return year;
    }

    public String toString() {
        return String.format("《%s》 by %s (ISBN: %s, Year: %d)", title, author, isbn, year);
    }
}

class Library {
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
        System.out.println("✅ 图书添加成功！");
    }

    public void removeBook(String isbn) {
        boolean removed = books.removeIf(book -> book.getIsbn().equals(isbn));
        if (removed) {
            System.out.println("✅ 图书已成功删除！");
        } else {
            System.out.println("❌ 未找到对应 ISBN 的图书。");
        }
    }

    public void searchBookByTitle(String title) {
        System.out.println("🔍 搜索结果：");
        boolean found = false;
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                System.out.println(book);
                found = true;
            }
        }
        if (!found) {
            System.out.println("❌ 没有找到匹配的图书。");
        }
    }

    public void listAllBooks() {
        if (books.isEmpty()) {
            System.out.println("📚 图书馆目前没有图书。");
        } else {
            System.out.println("📖 所有图书列表：");
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    public void saveAndExit() {
        // 模拟保存功能
        System.out.println("💾 正在保存数据...（模拟）");
        System.out.println("👋 已退出系统。");
    }
}

public class LibraryManager {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Library library = new Library();

    public static void main(String[] args) {
        int choice = -1;
        while (choice != 5) {
            showMenu();
            try {
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        addBookProcess();
                        break;
                    case 2:
                        removeBookProcess();
                        break;
                    case 3:
                        searchBookProcess();
                        break;
                    case 4:
                        library.listAllBooks();
                        break;
                    case 5:
                        library.saveAndExit();
                        break;
                    default:
                        System.out.println("❗ 无效选项，请输入 1~5 之间的数字。");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ 输入错误，请输入数字。");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n========== 图书管理系统 ==========");
        System.out.println("1. 添加图书");
        System.out.println("2. 删除图书（通过 ISBN）");
        System.out.println("3. 查找图书（通过标题）");
        System.out.println("4. 显示所有图书");
        System.out.println("5. 保存并退出");
        System.out.print("请输入选项（1-5）：");
    }

    private static void addBookProcess() {
        System.out.print("请输入图书标题：");
        String title = scanner.nextLine();
        System.out.print("请输入作者名称：");
        String author = scanner.nextLine();
        System.out.print("请输入 ISBN 编号：");
        String isbn = scanner.nextLine();
        System.out.print("请输入出版年份：");
        int year = Integer.parseInt(scanner.nextLine());

        Book book = new Book(title, author, isbn, year);
        library.addBook(book);
    }

    private static void removeBookProcess() {
        System.out.print("请输入要删除图书的 ISBN 编号：");
        String isbn = scanner.nextLine();
        library.removeBook(isbn);
    }

    private static void searchBookProcess() {
        System.out.print("请输入图书标题关键词：");
        String title = scanner.nextLine();
        library.searchBookByTitle(title);
    }
}

