/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Work-Game
 */
public class Atajos implements KeyListener{
    boolean control = false;
    boolean shif = false;
    boolean A = false;
    boolean[] keys = new boolean[256];
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Codigo: "+e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
