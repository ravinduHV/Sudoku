// Crook's algorithm

package com.mycompany.sudokugame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class mainWindow extends JFrame {
    private JPanel container;
    private List<List <cell>> Cells = new ArrayList<>();
    private List<List <cell>> Solution = new ArrayList<>();
    private int difficulty;
    private int resolution = 70;

    mainWindow(){
        this.setTitle("Sudoku");
        this.setSize(9*resolution,9*resolution + 90);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0,0));

        JPanel mainPanel = new JPanel();
        mainPanel.setSize(9*resolution,9*resolution);
        mainPanel.setLayout(new BorderLayout(0,0));
        this.add(mainPanel, BorderLayout.CENTER);

        JPanel BottonPanel = new JPanel();
        BottonPanel.setSize(9*resolution,90);
        BottonPanel.setLayout(new GridLayout(1,5,1,1));
        this.add(BottonPanel, BorderLayout.SOUTH);

        
        container = mainPanel;

        //generate buttons to New, Reset, Show Solutions
        JButton NewGame = new JButton("New Game");
        NewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                newGame();
            }
        });
        
        JButton Reset = new JButton("Reset");
        Reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                reset();
            }
        });

        JButton ShowSolution = new JButton("Show Solution");
        ShowSolution.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                solve();
            }
        });


        BottonPanel.add(new JPanel());
        BottonPanel.add(NewGame);
        BottonPanel.add(Reset);
        BottonPanel.add(ShowSolution);
        BottonPanel.add(new JPanel());

        container.setBackground(Color.WHITE);
        
        newGame();
        
        this.setVisible(true);
    }

    public void newGame(){
        Cells.clear();
        String[] options = new String[]{"Easy", "Medium", "Hard"};
        String difficultyLevel = null;

        while (difficultyLevel == null) {
            difficultyLevel = (String) JOptionPane.showInputDialog(
                null, 
                "Select Difficulty Level", 
                "Difficulty Level", 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                options, 
                options[0]
            );
        }
        if(difficultyLevel.equals("Easy")){
            selectDifficulty(1);
        }else if(difficultyLevel.equals("Medium")){
            selectDifficulty(2);
        }else if(difficultyLevel.equals("Hard")){
            selectDifficulty(3);
        }

        container.removeAll();
        createCells();
        GenerateSudokuPuzzle();
        print2DCells();
        JPanel mainSprint = gameWindow();
        container.add(mainSprint);
        container.revalidate();
        container.repaint();
    }

    public void reset(){
        //remove all tempValues
        for (List<cell> cells:Cells){
            for (cell cell_:cells){
                cell_.setTempValue(0);
            }
        }
        JPanel mainSprint = gameWindow();
        container.removeAll();
        container.add(mainSprint);
        container.revalidate();
        container.repaint();

    }

    public void createCells(){
        Boolean b = false;
        for(int i=0; i<9; i++){
            if (i%3 == 0){
                b = !b;
            }
            Boolean c = false;
            List<cell> cells = new ArrayList<>();
            for(int j=0; j<9; j++){
                if (j%3 == 0){
                    c = !c;
                }
                cell newCell = new cell(i,j);
                if(b){
                    newCell.setColor(c? Color.gray: Color.LIGHT_GRAY);
                }else{
                    newCell.setColor(c? Color.LIGHT_GRAY: Color.gray);
                }
                cells.add(newCell);
            }
            Cells.add(cells);
        }
    }

    public JPanel gameWindow() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(9,9,1,1));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                final int k = i;
                final int l = j;
                
                JPanel pn = Cells.get(i).get(j).createCell();
                pn.setVisible(true);
                mainPanel.add(pn);
                //system out text when  Cells.get(i).get(j).textArea is focused;
                if (Cells.get(k).get(l).getValue() == 0){
                    Cells.get(k).get(l).textArea.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusGained(java.awt.event.FocusEvent evt) {
                            ValidateSubSets();
                        }
                    });
                }
            }
        }
        return mainPanel;
    }


    

    public void assignRandomValues(){
        //for some cells of sudoku puzzle assign random values
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(Math.random() > 0.5){
                    Cells.get(i).get(j).setValue((int)(Math.random()*9)+1);
                }
            }
        }
    }

    public Boolean ValidateSubSets(){
        // Create list of all subsets (rows, columns, and 3x3 grids)
        List<List<cell>> subsets = new ArrayList<>();
        for(int i=0; i<9; i++){
            List<cell> row = new ArrayList<>();
            List<cell> column = new ArrayList<>();
            List<cell> square = new ArrayList<>();
            for(int j=0; j<9; j++){
                row.add(Cells.get(i).get(j));
                column.add(Cells.get(j).get(i));
                square.add(Cells.get((i/3)*3 + j/3).get((i%3)*3 + j%3));
            }
            subsets.add(row);
            subsets.add(column);
            subsets.add(square);
        }
        
        boolean isValid = true;
    
        // Validate each subset (row, column, and 3x3 grid)
        for(List<cell> subset : subsets){
            // Use a temporary array to track duplicates
            int[] counts = new int[10]; // for digits 1 through 9
            for(cell currentCell : subset){
                int value = currentCell.getAnyValue();
                if(value != 0){
                    counts[value]++;
                    if(counts[value] > 1 && currentCell.textArea != null){
                        currentCell.textArea.setBackground(Color.RED); // Mark as duplicate
                        isValid = false;
                    }
                }
            }
        }
    
        // Reset color of cells that are not in conflict
        for (List<cell> cells : Cells){
            for (cell cell_ : cells){
                if(cell_.getAnyValue() != 0 && cell_.textArea != null && cell_.textArea.getBackground() == Color.RED){
                    // Check if this cell is actually part of a duplicate subset
                    boolean isInConflict = false;
                    for(List<cell> subset : subsets){
                        int count = 0;
                        for(cell subCell : subset){
                            if(subCell.getAnyValue() == cell_.getAnyValue()){
                                count++;
                            }
                        }
                        if(count > 1){
                            isInConflict = true;
                            break;
                        }
                    }
                    // If no conflict, reset the color
                    if(!isInConflict){
                        cell_.textArea.setBackground(cell_.getColor());
                    }
                }
            }
        }
    
        return isValid;
    }



    public void GenerateSudokuPuzzle() {
        fillGrid(0, 0);
        //copy the Cells to solution. deep copy
        for (int i = 0; i < 9; i++) {
            List<cell> row = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                cell newCell = new cell(i,j);
                newCell.setValue(Cells.get(i).get(j).getValue());
                row.add(newCell);
            }
            Solution.add(row);
        }
        SetValueZeroOfRandomCells(81-difficulty);
    }

    public void SetValueZeroOfRandomCells(int noOfCells){
        for(int i=0; i<noOfCells; i++){
            int row = (int)(Math.random()*9);
            int col = (int)(Math.random()*9);
            Cells.get(row).get(col).setValue(0);
        }
    }

    private boolean fillGrid(int row, int col) {
        if (col == 9) {
            col = 0;
            if (++row == 9) {
                return true;
            }
        }
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        for (int number : numbers) {
            if (isSafe(row, col, number)) {
                Cells.get(row).get(col).setValue(number);
                if (fillGrid(row, col + 1)) {
                    return true;
                }
            }
        }
        Cells.get(row).get(col).setValue(0);
        return false;
    }

    private boolean isSafe(int row, int col, int number) {
        for (int i = 0; i < 9; i++) {
            if (Cells.get(row).get(i).getValue() == number ||
                Cells.get(i).get(col).getValue() == number ||
                Cells.get((row / 3) * 3 + i / 3).get((col / 3) * 3 + i % 3).getValue() == number) {
                return false;
            }
        }
        return true;
    }



    public void solve(){
        //show Solution of the sudoku puzzle
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Cells.get(i).get(j).setValue(Solution.get(i).get(j).getValue());
            }
        }
        print2DCells();
        container.removeAll();
        JPanel mainSprint = gameWindow();
        container.add(mainSprint);
        container.revalidate();
        container.repaint();
    }

    public void print2DCells(){
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                System.out.print(Cells.get(i).get(j).getValue() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void selectDifficulty(int difficultyLevel){
        if(difficultyLevel == 1){
            //random number between 35-45
            difficulty = (int)(Math.random()*10)+35;
        }else if(difficultyLevel == 2){
            // random number between 30-40
            difficulty = (int)(Math.random()*10)+30;
        }else if(difficultyLevel == 3){
            // random number between 25-35
            difficulty = (int)(Math.random()*10)+25;
        }
    }

}
