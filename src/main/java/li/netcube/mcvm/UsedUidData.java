package li.netcube.mcvm;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.Iterator;

public class UsedUidData extends WorldSavedData{

    private static final String IDENTIFIER = MCVM.MODID;

    private ArrayList<Integer> usedUids = new ArrayList<Integer>();

    private UsedUidData()
    {
        this(IDENTIFIER);
    }

    private UsedUidData(String parIdentifier)
    {
        super(parIdentifier);
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        // DEBUG
        //System.out.println("WorldData readFromNBT");

        usedUids = intArrayToList(nbt.getIntArray("usedUids"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        // DEBUG
        //System.out.println("MagicBeansWorldData writeToNBT");

        nbt.setIntArray("usedUids", intListToArray(usedUids));

        return nbt;

    }

    public static UsedUidData get(World world)
    {
        UsedUidData data = (UsedUidData) world.loadData(UsedUidData.class, IDENTIFIER);
        if (data == null)
        {
            // DEBUG
            //System.out.println("UsedUidData didn't exist so creating it");

            data = new UsedUidData();
            world.setData(IDENTIFIER, data);
        }
        return data;
    }

    public Integer getNextFree() {
        //System.out.println(usedUids);
        for (Integer i = 0; ; i++) {
            if (!usedUids.contains(i)) {
                usedUids.add(i);
                //System.out.println(usedUids);
                markDirty();
                return i;
            }
        }
    }

    public void freeUid(Integer uid) {
        //System.out.println(usedUids);
        usedUids.remove(uid);
        //System.out.println(usedUids);
        markDirty();
    }

    private static int[] intListToArray(ArrayList<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next();
        }
        return ret;
    }

     private static ArrayList<Integer> intArrayToList(int integers[])
    {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < integers.length; i++) {
            ret.add(integers[i]);
        }
        return ret;
    }
}
