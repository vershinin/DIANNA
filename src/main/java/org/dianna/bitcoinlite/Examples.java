/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dianna.bitcoinlite;
import com.google.bitcoin.core.*;
import com.google.bitcoin.store.BlockStoreException;
import com.google.bitcoin.store.BoundedOverheadBlockStore;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 *
 * @author pentarh
 */
public class Examples {
    public static void main(String[] args) {
        final NetworkParameters params = NetworkParameters.testNet3();
        final File walletfile = new File("wallet.dat");
        Wallet wallet;
        System.out.println("Loading wallet");
        try {
            wallet=Wallet.loadFromFile(walletfile);
        } catch (IOException e) {
            System.out.println("Wallet not found, creating");
            wallet = new Wallet(params);
            wallet.keychain.add(new ECKey());
            try {
                wallet.saveToFile(walletfile);
            } catch (IOException e1) {
                System.out.println("Cant save wallet!");
                return;
            }
        }
        final File blockstorefile = new File("blocks.dat");
        System.out.println("Loading blockstore");
        BlockChain chain;
        try {
            BoundedOverheadBlockStore blockstore=new BoundedOverheadBlockStore(params,blockstorefile);
            chain=new BlockChain(params,wallet,blockstore);
        } catch (BlockStoreException e) {
            System.out.println("ERROR: "+e.getMessage());
            return;
        }
      // byte[] chainHead = chain.getChainHead().getHeader().getTransactions().get(0).getInputs().get(0).getScriptBytes();
      //  System.out.println(chainHead);
        
        final PeerGroup peerGroup=new PeerGroup(params,chain){
        	
        };
        try {
            peerGroup.addAddress(new PeerAddress(InetAddress.getLocalHost()));
        } catch (UnknownHostException e) {
            System.out.println("ERROR: "+e.getMessage());
        }
        peerGroup.downloadBlockChain();
    }
}
