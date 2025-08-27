/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_scanner;

import pos_scanner.dao.DAO.SyncStatus;

/**
 *
 * @author Server
 */
public class Model
{

    protected int id;
    SyncStatus status = SyncStatus.SAME;

    public Model()
    {
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public SyncStatus getSyncStatus()
    {
        return this.status;
    }

    /*
        public enum SyncStatus
    {
        SAME,
        MODIFIED,
        ADDED,
        DELETED
    }
     */
    public void setSyncStatus(SyncStatus status)
    {
        if (status.ordinal() > this.status.ordinal() && status.ordinal() <= SyncStatus.ADDED.ordinal())
        {
            this.status = status;
        }
    }
}
