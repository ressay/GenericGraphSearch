package company;

import java.util.LinkedList;

/**
 *
 * Created by ressay on 19/04/18.
 */
public class Interval
{
    int min,max,div;
    Interval[] inner = null;
    int[] indexes = null;
    int limit = 0;
    int call = 0;

    public Interval(int min, int max,int div)
    {
        this.min = min;
        this.max = max;
        this.div = div;
    }

    void createInnerIntervals()
    {
        inner = new Interval[div];
        indexes = new int[div];
        limit = div;
        for (int i = 0; i < div; i++)
        {
            int diff = (max - min) / div;
            int end = (i == div-1)?max:(min + diff*(i+1));
            inner[i] = new Interval(min + diff*i,end,div);
            indexes[i] = 0;
        }
    }

    LinkedList<Interval> getIntervals()
    {
        LinkedList<Interval> intervals = getIntervals(call);
        call++;
        return intervals;
    }

    LinkedList<Interval> getIntervals(int call)
    {
        LinkedList<Interval> intervals = new LinkedList<>();
        if(call < div)
        {
            int diff = (max - min) / div;
            int end = (call == div-1)?max:(min + diff*(call+1));
            intervals.add(new Interval(min + diff*call,end,0));
        }
        else
        {
            if(inner == null)
                createInnerIntervals();

        }
        return intervals;
    }
}
