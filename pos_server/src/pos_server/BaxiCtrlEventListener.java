/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_server;

/**
 *
 * @author Laptop
 */
import eu.nets.baxi.client.*;
import java.util.EventListener;

public interface BaxiCtrlEventListener extends EventListener {

    public void OnStdRspReceived(StdRspReceivedEventArgs srrea);

    public void OnPrintText(PrintTextEventArgs ptea);

    public void OnDisplayText(DisplayTextEventArgs dtea);

    public void OnLocalMode(LocalModeEventArgs lmea);

    public void OnTerminalReady(TerminalReadyEventArgs trea);

    public void OnTLDReceived(TLDReceivedEventArgs tldrea);

    public void OnLastFinancialResult(LastFinancialResultEventArgs lfrea);

    public void OnBaxiError(BaxiErrorEventArgs beea);

    public void OnJsonReceived(JsonReceivedEventArgs jrea);

    public void OnBarcodeReader(BarcodeReaderEventArgs brea);
}
