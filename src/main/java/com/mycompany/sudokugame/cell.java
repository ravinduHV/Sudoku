package com.mycompany.sudokugame;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class cell{
    private Color color;
    private Color tempColor;
    private int X;
    private int Y;
    private int value = 0;
    private int TempValue = 0;
    public JTextField textArea;

    cell(int x, int y){
        this.X = x;
        this.Y = y;
        
    }

    cell(int x, int y, Color color){
        this.X = x;
        this.Y = y;
        this.color = color;
    }

    public void settempColor(Color color){
        this.tempColor = color;
    }


    public void setValue(int value){
        this.value = value;
    }
    
    public void setColor(Color color){
        this.color = color;
    }

    public void setTempValue(int value){
        this.TempValue = value;
    }
    
    public JPanel createCell(){
        JPanel cell = new JPanel();
        cell.setLayout(new BorderLayout(0,0));
        cell.setSize(50,50);
        if (this.value != 0){
            JLabel jl = new JLabel(String.valueOf(this.value));
            //center text
            jl.setHorizontalAlignment(SwingConstants.CENTER);
            jl.setVerticalAlignment(SwingConstants.CENTER);

            cell.add(jl, BorderLayout.CENTER);
        }else {
            textArea = new JTextField(1);
            textArea.setHorizontalAlignment(SwingConstants.CENTER);
            textArea.setBackground(color);
            cell.add(textArea, BorderLayout.CENTER);
            if (TempValue != 0){
                textArea.setText(String.valueOf(TempValue));
            }
            //update TempValue when user types in the cell 
            textArea.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    try {
                        TempValue = validate(Integer.parseInt(textArea.getText()));
                        System.out.println(TempValue);
                    } catch (NumberFormatException ex) {
                        TempValue = 0;
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    try {
                        TempValue = validate(Integer.parseInt(textArea.getText()));
                        System.out.println(TempValue);
                    } catch (NumberFormatException ex) {
                        TempValue = 0;
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    try {
                        TempValue = validate(Integer.parseInt(textArea.getText()));
                        System.out.println(TempValue);
                    } catch (NumberFormatException ex) {
                        TempValue = 0;
                    }
                }
                
                
            });
        }
        cell.setBackground(this.color);
        return cell;
    }

    public int getValue(){
        return this.value;
    }

    public Color getColor(){
        return this.color;
    }
    public int getX(){
        return this.X;
    }

    public int getY(){
        return this.Y;
    }

    public int getTempValue(){
        return this.TempValue;
    }

    public String toString(){
        return "X: " + this.X + " Y: " + this.Y + " Value: " + this.value + " TempValue: " + this.TempValue +".";
    }

    public int validate(int i){
        if(i>0 && i<10){
            return i;
        }
        return 0;
    }

    public int getAnyValue(){
        if(value != 0){
            return value;
        }
        return TempValue;
    }

    public Color gettempColor(){
        return this.tempColor;
    }

}
