package sorter;

import entity.Customer;

import java.util.Comparator;

public class CustomerSorter implements Comparator<Customer> {
    @Override
    public int compare(Customer c1, Customer c2) {
        int comp = c1.lastName().compareTo(c2.lastName());

        return comp;
    }
}
