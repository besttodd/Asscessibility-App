package au.edu.jcu.cp3405.prototype;

import java.util.Comparator;

public class SortBasedOnName implements Comparator
{
    public int compare(Object o1, Object o2)
    {
        Contact c1 = (Contact) o1;// where FBFriends_Obj is your object class
        Contact c2 = (Contact) o2;
        return c1.name.compareToIgnoreCase(c2.name);//where uname is field name
    }
}
