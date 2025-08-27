/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client;

import java.util.Map;
import pos_client.common.Core;
import pos_client.db.dao.ReceiptDAO;
import pos_client.db.dao.ReceiptTypeDAO.ReceiptType;
import pos_client.db.dao.UserDAO;
import pos_client.windows.Log;

/**
 *
 * @author Laptop
 */
public class UserModel
{

    private int id;
    private String name;
    private String password;
    private int employeeId;
    private float exchange = 0;

    public enum SecurityLevel
    {
        ADMIN,
        DEVELOPER,
        CONTROLLER,
        USER
    }
    private SecurityLevel securityLevel;

    public enum CashboxType
    {

        MANUAL,
        AUTOMATIC
    }

    private Receipt receipt = null;
    private Receipt bord = null;

    public UserModel()
    {
    }

    public UserModel(SecurityLevel level)
    { // For logg database rebuild
        this.securityLevel = level;
    }

    public UserModel(int id,
            String name,
            String password,
            SecurityLevel level,
            int employeeId)
    {
        this.id = id;
        this.name = name;
        this.password = password;
        this.securityLevel = level;
        this.employeeId = employeeId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }

    public void setSecuritylevel(SecurityLevel securityLevel)
    {
        this.securityLevel = securityLevel;
    }

    public SecurityLevel getSecurityLevel()
    {
        return this.securityLevel;
    }

    public String getSecurityLevelStr()
    {
        String securityLevel = "ERROR";

        UserDAO userDAO = new UserDAO();
        Map<String, SecurityLevel> securitylevel = userDAO.getSecuritylevel();

        for (Map.Entry<String, SecurityLevel> entry : securitylevel.entrySet())
        {
            if (entry.getValue() == this.securityLevel)
            {
                securityLevel = entry.getKey();
                break;
            }
        }

        return securityLevel;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setCurrentReceipt(Receipt receipt)
    {
        this.receipt = receipt;
    }

    public Receipt getCurrentReceipt()
    {
        return receipt;
    }

    public void setCurrentBord(Receipt bord)
    {
        this.bord = bord;
    }

    public Receipt getCurrentBord()
    {
        return bord;
    }

    public void setEmployeeId(int employeeId)
    {
        this.employeeId = employeeId;
    }

    public int getEmployeeId()
    {
        return employeeId;
    }

    public void setExchange(float exchange)
    {
        this.exchange = exchange;
    }

    public float getExchange()
    {
        return exchange;
    }

    public void newReceipt()
    {
        if (receipt == null)
        {
            Core.getInstance().getLog().log("Skapar nytt kvitto", Log.LogLevel.NORMAL);
            receipt = new Receipt(this, true);
        } else
        {
            Core.getInstance().getLog().log("Det finns redan ett aktivtkvitto!", Log.LogLevel.CRITICAL);
        }
    }

    public void cancelReceipt()
    {
        if (receipt != null)
        {
            receipt.cancel();
            resetReceipt();
        }
    }

    public void parkReceipt()
    {
        if (receipt != null)
        {
            receipt.park();
            resetReceipt();
        }
    }

    public void resetReceipt()
    {
        receipt = null;
        newReceipt();
    }

    public void swapReceipt(Receipt parkedReceipt)
    {
        if (receipt.hasSales())
        {
            receipt.changeReceiptTypeId(ReceiptType.PARKED);
        } else
        {
            ReceiptDAO receiptDAO = new ReceiptDAO();
            receiptDAO.remove(receipt.getReceiptId());
        }

        receipt = null;
        receipt = parkedReceipt;
        receipt.changeReceiptTypeId(ReceiptType.ONGOING);
    }

    public void cashboxOpen(CashboxType type)
    {
        UserDAO userDAO = new UserDAO();
        userDAO.cashboxOpen(this, type);
    }

    public UserDAO.CashboxOpened cashboxOpened()
    {
        UserDAO userDAO = new UserDAO();
        return userDAO.cashboxOpened(this);
    }

}
