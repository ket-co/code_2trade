import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
class StockScreener {
    final String stockInfoFile="./stockInfo.txt";
    final String mutualfundInfoFile="./mutualfundInfo.txt";
    List<List<Object>> originalInfoList=new ArrayList<>();
    List<List<Object>> infoList=new ArrayList<>();
    List<List<Object>> tempInfoList=new ArrayList<>();
    int selectedFilterIndex=3;
    JButton nameListButton[];
    JLabel filterListLabel[];
    Color errorColor=new Color(230,0,0);
    String stockCompLabels[]={
        "Name","Sector","Current Price","Market Cap","1Y Return","3Y Return","5Y Return","Intrinsic Value",
        "P/E","P/B","D/E","Current Ratio","Interest Coverage Ratio","Return On Equity","Return On Capital Employed",
        "Net Profit Margin","Cash Flow Margin","Earning Per Share","EBITDA","PBIT","Revenue","Net Profit","Dividend Yield",
        "Current Assets","Current Liabilities","Promoter Holding","FII Holding","DII Holding","Retail Holding"
    };
    String MFComptLabels[]={
        "Name","Category (Equity/Dept/Etc)","Category (Large/Mid/Etc)","Net Asset Value","1Y Return","3Y Return",
        "5Y Return","Lock In","Fund Size","Min Investment","Expense Ratio","Exit Load","P/E","P/B",
        "Alpha","Beta","Sharpe Ratio","Sartino"
    };
    void MainFrame(){
        MyFrame myFrame=new MyFrame();
        String buttonContents[]={"Add Stocks","Add MutualFund","Search Stocks","Search MutualFund","Stock Watchlist","MutualFund WatchList"};
        JButton mainFrameButtons[]=new JButton[buttonContents.length];
        myFrame.calcMeasure();
        myFrame.addTopLabel("Stock Screener");
        myFrame.addExitButton();

        for(int i=0;i<buttonContents.length;i++){
            mainFrameButtons[i]=new JButton(buttonContents[i]);
            mainFrameButtons[i].setBounds(MyFrame.frameWidth/3+50,MyFrame.frameHeight/3+i*(myFrame.fontSize+15),400,myFrame.fontSize+5);
            mainFrameButtons[i].setBackground(myFrame.compBackColor);
            mainFrameButtons[i].setForeground(myFrame.compForeColor);
            mainFrameButtons[i].setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
            mainFrameButtons[i].setBorder(myFrame.borderColor);
            myFrame.add(mainFrameButtons[i]);
            final int index=i;
            mainFrameButtons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    switch(index){
                        case 0:AddStockFrame();break;
                        case 1:AddMutualFundFrame();break;
                        case 2:SearchStockFrame();break;
                        case 3:SearchMutualFundFrame();break;
                        case 4:StockWatchListFrame();break;
                        case 5:MutualFundWatchListFrame();break;
                    }
                    myFrame.dispose();
                }
            });
        }
        if(NoOfLinesInFile(stockInfoFile)==0){
            mainFrameButtons[2].setEnabled(false);
            mainFrameButtons[3].setEnabled(false);
        }
        if(NoOfLinesInFile(mutualfundInfoFile)==0){
            mainFrameButtons[4].setEnabled(false);
            mainFrameButtons[5].setEnabled(false);
        }
    }
    String SpaceToUnderScore(String str){
        return str.replace(' ', '_');
    }
    String UnderScoreToSpace(String str){
        return str.replace('_', ' ');
    }
    boolean IsFloat(String str){
        if(str.isEmpty()) return true;
        try{
            Float.parseFloat(str);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    boolean IsDigit(String str){
        try{
            Float.parseFloat(str);
            return true;
        }catch(Exception e){}
        return false;
    }
    void setTempListInfo(boolean isForWL){
        if(isForWL){
            for(int i=0;i<infoList.size();i++){
                if(Boolean.valueOf(infoList.get(i).get(infoList.get(0).size()-1).toString()))
                tempInfoList.add(infoList.get(i));
            }
            infoList.clear();
            for(int i=0;i<tempInfoList.size();i++){
                infoList.add(tempInfoList.get(i));
            }
        }else{
            for(int i=0;i<infoList.size();i++){
                tempInfoList.add(infoList.get(i));
            }
        }
    }
    void UpdateAllInfoList(int index,PairComponent pc[],boolean isWL){
        String str=originalInfoList.get(index).get(0).toString();
        for(int i=0;i<originalInfoList.get(index).size()-1;i++){
            originalInfoList.get(index).set(i,SpaceToUnderScore(pc[i].textField.getText().toString()));
        }
        originalInfoList.get(index).set(originalInfoList.get(index).size()-1,String.valueOf(isWL));
        for(int i=0;i<infoList.size();i++){
            if(infoList.get(i).get(0).toString().equals(str)){
                index=i;
                break;
            }
        }
        for(int i=0;i<infoList.get(index).size()-1;i++){
            infoList.get(index).set(i,SpaceToUnderScore(pc[i].textField.getText().toString()));
        }
        infoList.get(index).set(infoList.get(index).size()-1,String.valueOf(isWL));
        for(int i=0;i<tempInfoList.size();i++){
            if(tempInfoList.get(i).get(0).toString().equals(str)){
                index=i;
                break;
            }
        }
        for(int i=0;i<tempInfoList.get(index).size()-1;i++){
            tempInfoList.get(index).set(i,SpaceToUnderScore(pc[i].textField.getText().toString()));
        }
        tempInfoList.get(index).set(tempInfoList.get(index).size()-1,String.valueOf(isWL));
    }
    void RemoveRowFromAllList(int index){
        String str=originalInfoList.get(index).get(0).toString();
        originalInfoList.remove(index);

        for(int i=0;i<infoList.size();i++){
            if(infoList.get(i).get(0).toString().equals(str)){
                index=i;
                break;
            }
        }
        infoList.remove(index);

        for(int i=0;i<tempInfoList.size();i++){
            if(tempInfoList.get(i).get(0).toString().equals(str)){
                index=i;
                break;
            }
        }
        tempInfoList.remove(index);
    }
    int NoOfLinesInFile(String path){
        int c=0;
        try{
            File f=new File(path);
            Scanner sc=new Scanner(f);
            while(sc.hasNextLine()){
                if(sc.nextLine().isEmpty()){
                    continue;
                }
                c++;
            }
            sc.close();
            return c;
        }catch(Exception e){}
        return c;
    }
    void ReadFromFile(String path,int noOfRatios){
        try{
            File file=new File(path);
            Scanner sc=new Scanner(file);
            String str;
            for(int i=0;sc.hasNextLine();i++){
                infoList.add(new ArrayList<>());
                originalInfoList.add(new ArrayList<>());
                for(int j=0;j<noOfRatios;j++){
                    str=sc.next();
                    infoList.get(i).add(str);
                    originalInfoList.get(i).add(str);
                }
            }
            sc.close();
        }catch(Exception e){}
        if(infoList.get(infoList.size()-1).isEmpty()){
            infoList.remove(infoList.size()-1);
            originalInfoList.remove(originalInfoList.size()-1);
        }
    }
    void WriteToFile(PairComponent pc[],String cLs[],boolean isWL,String filePath){
        try{
            FileWriter fw=new FileWriter(filePath,true);
            for(int i=0;i<cLs.length;i++){
                String str=pc[i].textField.getText();
                if(str.isEmpty()){
                    fw.write("0 ");
                }else{
                    fw.write(SpaceToUnderScore(str)+" ");
                }
                pc[i].textField.setText(null);
            }
            fw.write(String.valueOf(isWL)+"\n");
            fw.close();
        }catch(Exception e){}
    }
    void WriteToFileByList(String filePath){
        try{
            FileWriter fw=new FileWriter(filePath);
            for(int i=0;i<originalInfoList.size();i++){
                for(int j=0;j<originalInfoList.get(0).size();j++){
                    fw.write(originalInfoList.get(i).get(j)+" ");
                }
                fw.write("\n");
            }
            fw.close();
        }catch(Exception e){}
    }
    void Sort(boolean isAscending){
        if(IsDigit(tempInfoList.get(0).get(selectedFilterIndex).toString())){
            BubbleSortForNumber(isAscending);
        }else{
            BubbleSortForString(isAscending);
        }
    }
    void BubbleSortForString(boolean isAscending){
        int n=tempInfoList.size();
        for(int i=0;i<n-1;i++){
            for(int j=0;j<n-i-1;j++){
                if(isAscending){
                    if(tempInfoList.get(j).get(selectedFilterIndex).toString().compareToIgnoreCase(tempInfoList.get(j+1).get(selectedFilterIndex).toString())>0){
                        List<Object> tempRow=tempInfoList.get(j);
                        tempInfoList.set(j,tempInfoList.get(j+1));
                        tempInfoList.set(j+1,tempRow);
                    }
                }else{
                    if(tempInfoList.get(j).get(selectedFilterIndex).toString().compareToIgnoreCase(tempInfoList.get(j+1).get(selectedFilterIndex).toString())<0){
                        List<Object> tempRow=tempInfoList.get(j);
                        tempInfoList.set(j,tempInfoList.get(j+1));
                        tempInfoList.set(j+1,tempRow);
                    }
                }
            }
        }
    }
    void BubbleSortForNumber(boolean isAscending){
        int n=tempInfoList.size();
        for(int i=0;i<n-1;i++){
            for(int j=0;j<n-i-1;j++){
                if(isAscending){
                    if(Double.valueOf(tempInfoList.get(j).get(selectedFilterIndex).toString())>Double.valueOf(tempInfoList.get(j+1).get(selectedFilterIndex).toString())){
                        List<Object> tempRow=tempInfoList.get(j);
                        tempInfoList.set(j,tempInfoList.get(j+1));
                        tempInfoList.set(j+1,tempRow);
                    }
                }else{
                    if(Double.valueOf(tempInfoList.get(j).get(selectedFilterIndex).toString())<Double.valueOf(tempInfoList.get(j+1).get(selectedFilterIndex).toString())){
                        List<Object> tempRow=tempInfoList.get(j);
                        tempInfoList.set(j,tempInfoList.get(j+1));
                        tempInfoList.set(j+1,tempRow);
                    }
                }
            }
        }
    }
    void AddStockFrame(){
        MyFrame myFrame=new MyFrame();
        JLabel stockNameLabel;
        JButton saveButton;
        PairComponent pc[]=new PairComponent[stockCompLabels.length];
        myFrame.addTopLabel("Add Stock");
        myFrame.addBackButton();
        myFrame.addExitButton();
        myFrame.addWatchListStatusButton();

        stockNameLabel=new JLabel();
        stockNameLabel.setBounds(0,myFrame.fontSize+50,MyFrame.frameWidth,myFrame.fontSize+10);
        stockNameLabel.setOpaque(true);
        stockNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        stockNameLabel.setBackground(Color.BLACK);
        stockNameLabel.setForeground(Color.WHITE);
        stockNameLabel.setFont(new Font("Times New Roman",Font.PLAIN,myFrame.fontSize+5));
        stockNameLabel.setBorder(myFrame.borderColor);
        myFrame.add(stockNameLabel);

        saveButton=new JButton("Save");
        saveButton.setBounds(MyFrame.frameWidth/2-50,MyFrame.frameHeight-myFrame.fontSize-5,100,myFrame.fontSize);
        saveButton.setBackground(myFrame.compBackColor);
        saveButton.setForeground(myFrame.compForeColor);
        saveButton.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        saveButton.setBorder(myFrame.borderColor);
        myFrame.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                WriteToFile(pc,stockCompLabels,myFrame.isWatchListed,stockInfoFile);
            }
        });

        for(int i=0;i<stockCompLabels.length;i++){
            pc[i]=new PairComponent(stockCompLabels[i]);
            pc[i].adjustWidthOnText(stockCompLabels[i]);
            if(i<2){
                pc[i].setTextFieldWidth(500);
            }
            if(i<stockCompLabels.length-1){
                final int index=i;
                pc[i].textField.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e){
                        pc[index+1].textField.requestFocus();
                    }
                });
            }
            myFrame.addPairComponent(pc[i].label, pc[i].textField);
            final int index=i;
            pc[i].textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(index==0) stockNameLabel.setText(pc[0].textField.getText());
                    if(index>1){
                        if(IsFloat(pc[index].textField.getText())){
                            pc[index].textField.setBackground(pc[index].tfBackColor);
                            if(pc[index].isError){
                                pc[index].isError=false;
                                PairComponent.errorCounter--;
                            }
                        }else{
                            pc[index].textField.setBackground(errorColor);
                            if(!pc[index].isError){
                            pc[index].isError=true;
                            PairComponent.errorCounter++;
                            }
                        }
                    }
                    if(PairComponent.errorCounter==0){
                        pc[index].isError=false;
                        saveButton.setEnabled(true);
                    }else{
                        saveButton.setEnabled(false);
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(index==0) stockNameLabel.setText(pc[0].textField.getText());
                    if(index>1){
                        if(IsFloat(pc[index].textField.getText())){
                            pc[index].textField.setBackground(pc[index].tfBackColor);
                            if(pc[index].isError){
                                pc[index].isError=false;
                                PairComponent.errorCounter--;
                            }
                        }else{
                            pc[index].textField.setBackground(errorColor);
                            if(!pc[index].isError){
                            pc[index].isError=true;
                            PairComponent.errorCounter++;
                            }
                        }
                    }
                    if(PairComponent.errorCounter==0){
                        pc[index].isError=false;
                        saveButton.setEnabled(true);
                    }else{
                        saveButton.setEnabled(false);
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(index==0) stockNameLabel.setText(pc[0].textField.getText());
                    if(index>1){
                        if(IsFloat(pc[index].textField.getText())){
                            pc[index].textField.setBackground(pc[index].tfBackColor);
                            if(pc[index].isError){
                                pc[index].isError=false;
                                PairComponent.errorCounter--;
                            }
                        }else{
                            pc[index].textField.setBackground(errorColor);
                            if(!pc[index].isError){
                            pc[index].isError=true;
                            PairComponent.errorCounter++;
                            }
                        }
                    }
                    if(PairComponent.errorCounter==0){
                        pc[index].isError=false;
                        saveButton.setEnabled(true);
                    }else{
                        saveButton.setEnabled(false);
                    }
                }
            });
        }
        pc[0].textField.requestFocus();
    }
    void UpdateStockFrame(MyFrame stockFrame,String stockName){
        MyFrame myFrame=new MyFrame();
        JLabel stockNameLabel;
        JButton saveButton,deletButton;
        PairComponent pc[]=new PairComponent[stockCompLabels.length];
        int indexOfStockName=-1;
        myFrame.addTopLabel("Update Stock");
        myFrame.addBackButton();
        for (ActionListener al : myFrame.backButton.getActionListeners()) {
            myFrame.backButton.removeActionListener(al);
        }
        myFrame.backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                myFrame.dispose();
                PairComponent.currentXPos=5;
                PairComponent.currentYPos=150;
                PairComponent.prevXPos=5;
                PairComponent.prevYPos=150;
                PairComponent.errorCounter=0;
                stockFrame.setVisible(true);
            }
        });
        myFrame.addExitButton();
        myFrame.addWatchListStatusButton();
        stockFrame.setVisible(false);

        stockNameLabel=new JLabel(UnderScoreToSpace(stockName));
        stockNameLabel.setBounds(0,myFrame.fontSize+50,MyFrame.frameWidth,myFrame.fontSize+10);
        stockNameLabel.setOpaque(true);
        stockNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        stockNameLabel.setBackground(Color.BLACK);
        stockNameLabel.setForeground(Color.WHITE);
        stockNameLabel.setFont(new Font("Times New Roman",Font.PLAIN,myFrame.fontSize+5));
        stockNameLabel.setBorder(myFrame.borderColor);
        myFrame.add(stockNameLabel);

        saveButton=new JButton("Save");
        saveButton.setBounds(MyFrame.frameWidth/2-50,MyFrame.frameHeight-myFrame.fontSize-5,100,myFrame.fontSize);
        saveButton.setBackground(myFrame.compBackColor);
        saveButton.setForeground(myFrame.compForeColor);
        saveButton.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        saveButton.setBorder(myFrame.borderColor);
        myFrame.add(saveButton);

        deletButton=new JButton("Delete");
        deletButton.setBounds(MyFrame.frameWidth/2+MyFrame.frameWidth/4-100,MyFrame.frameHeight-myFrame.fontSize-5,100,myFrame.fontSize);
        deletButton.setBackground(myFrame.compBackColor);
        deletButton.setForeground(myFrame.compForeColor);
        deletButton.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        deletButton.setBorder(myFrame.borderColor);
        myFrame.add(deletButton);
        
        for(int i=0;i<originalInfoList.size();i++){
            if(originalInfoList.get(i).get(0).toString().equals(stockName)){
                indexOfStockName=i;
                break;
            }
        }
        myFrame.isWatchListed=Boolean.valueOf(originalInfoList.get(indexOfStockName).get(originalInfoList.get(indexOfStockName).size()-1).toString());
        if(myFrame.isWatchListed){
            myFrame.watchListStatus.setForeground(myFrame.compForeColor);
        }else{
            myFrame.watchListStatus.setForeground(Color.DARK_GRAY);
        }
        for(int i=0;i<stockCompLabels.length;i++){
            pc[i]=new PairComponent(stockCompLabels[i]);
            pc[i].adjustWidthOnText(stockCompLabels[i]);
            pc[i].textField.setText(UnderScoreToSpace(originalInfoList.get(indexOfStockName).get(i).toString()));
            if(i<2){
                pc[i].setTextFieldWidth(500);
            }
            if(i<stockCompLabels.length-1){
                final int index=i;
                pc[i].textField.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e){
                        pc[index+1].textField.requestFocus();
                    }
                });
            }
            myFrame.addPairComponent(pc[i].label, pc[i].textField);
            final int index=i;
            pc[i].textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(index==0) stockNameLabel.setText(pc[0].textField.getText());
                    if(index>1){
                        if(IsFloat(pc[index].textField.getText())){
                            pc[index].textField.setBackground(pc[index].tfBackColor);
                            if(pc[index].isError){
                                pc[index].isError=false;
                                PairComponent.errorCounter--;
                            }
                        }else{
                            pc[index].textField.setBackground(errorColor);
                            if(!pc[index].isError){
                                pc[index].isError=true;
                                PairComponent.errorCounter++;
                            }
                        }
                    }
                    if(PairComponent.errorCounter==0){
                        pc[index].isError=false;
                        saveButton.setEnabled(true);
                    }else{
                        saveButton.setEnabled(false);
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(index==0) stockNameLabel.setText(pc[0].textField.getText());
                    if(index>1){
                        if(IsFloat(pc[index].textField.getText())){
                            pc[index].textField.setBackground(pc[index].tfBackColor);
                            if(pc[index].isError){
                                pc[index].isError=false;
                                PairComponent.errorCounter--;
                            }
                        }else{
                            pc[index].textField.setBackground(errorColor);
                            if(!pc[index].isError){
                                pc[index].isError=true;
                                PairComponent.errorCounter++;
                            }
                        }
                    }
                    if(PairComponent.errorCounter==0){
                        pc[index].isError=false;
                        saveButton.setEnabled(true);
                    }else{
                        saveButton.setEnabled(false);
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(index==0) stockNameLabel.setText(pc[0].textField.getText());
                    if(index>1){
                        if(IsFloat(pc[index].textField.getText())){
                            pc[index].textField.setBackground(pc[index].tfBackColor);
                            if(pc[index].isError){
                                pc[index].isError=false;
                                PairComponent.errorCounter--;
                            }
                        }else{
                            pc[index].textField.setBackground(errorColor);
                            if(!pc[index].isError){
                            pc[index].isError=true;
                            PairComponent.errorCounter++;
                        }
                    }
                }
                if(PairComponent.errorCounter==0){
                    pc[index].isError=false;
                    saveButton.setEnabled(true);
                }else{
                    saveButton.setEnabled(false);
                }
            }
            });
        }
        final int finalIndexOfStockName=indexOfStockName;
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                UpdateAllInfoList(finalIndexOfStockName,pc,myFrame.isWatchListed);
                WriteToFileByList(stockInfoFile);
            }
        });
        deletButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                RemoveRowFromAllList(finalIndexOfStockName);
                WriteToFileByList(stockInfoFile);
                stockFrame.setVisible(true);
                PairComponent.currentXPos=5;
                PairComponent.currentYPos=150;
                PairComponent.prevXPos=5;
                PairComponent.prevYPos=150;
                PairComponent.errorCounter=0;
                myFrame.dispose();
                RefreshListPanel("");
            }
        });
        pc[0].textField.requestFocus();
    }
    void AddMutualFundFrame(){
        MyFrame myFrame=new MyFrame();
        JLabel stockNameLabel;
        JButton saveButton;
        PairComponent pc[]=new PairComponent[MFComptLabels.length];
        myFrame.addTopLabel("Add Mutual Fund");
        myFrame.addBackButton();
        myFrame.addExitButton();
        myFrame.addWatchListStatusButton();

        stockNameLabel=new JLabel();
        stockNameLabel.setBounds(0,myFrame.fontSize+50,MyFrame.frameWidth,myFrame.fontSize+10);
        stockNameLabel.setOpaque(true);
        stockNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        stockNameLabel.setBackground(Color.BLACK);
        stockNameLabel.setForeground(Color.WHITE);
        stockNameLabel.setFont(new Font("Times New Roman",Font.PLAIN,myFrame.fontSize+5));
        stockNameLabel.setBorder(myFrame.borderColor);
        myFrame.add(stockNameLabel);

        saveButton=new JButton("Save");
        saveButton.setBounds(MyFrame.frameWidth/2-50,MyFrame.frameHeight-myFrame.fontSize-5,100,myFrame.fontSize);
        saveButton.setBackground(myFrame.compBackColor);
        saveButton.setForeground(myFrame.compForeColor);
        saveButton.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        saveButton.setBorder(myFrame.borderColor);
        myFrame.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                WriteToFile(pc,MFComptLabels,myFrame.isWatchListed,mutualfundInfoFile);
            }
        });

        for(int i=0;i<MFComptLabels.length;i++){
            pc[i]=new PairComponent(MFComptLabels[i]);
            pc[i].adjustWidthOnText(MFComptLabels[i]);
            if(i<2){
                pc[i].setTextFieldWidth(500);
            }
            if(i<MFComptLabels.length-1){
                final int index=i;
                pc[i].textField.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e){
                        pc[index+1].textField.requestFocus();
                    }
                });
            }
            myFrame.addPairComponent(pc[i].label, pc[i].textField);
            
            final int index=i;
            pc[i].textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(index==0) stockNameLabel.setText(pc[0].textField.getText());
                    if(index>2){
                        if(IsFloat(pc[index].textField.getText())){
                            pc[index].textField.setBackground(pc[index].tfBackColor);
                            if(pc[index].isError){
                                pc[index].isError=false;
                                PairComponent.errorCounter--;
                            }
                        }else{
                            pc[index].textField.setBackground(errorColor);
                            if(!pc[index].isError){
                            pc[index].isError=true;
                            PairComponent.errorCounter++;
                            }
                        }
                    }
                    if(PairComponent.errorCounter==0){
                        pc[index].isError=false;
                        saveButton.setEnabled(true);
                    }else{
                        saveButton.setEnabled(false);
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(index==0) stockNameLabel.setText(pc[0].textField.getText());
                    if(index>2){
                        if(IsFloat(pc[index].textField.getText())){
                            pc[index].textField.setBackground(pc[index].tfBackColor);
                            if(pc[index].isError){
                                pc[index].isError=false;
                                PairComponent.errorCounter--;
                            }
                        }else{
                            pc[index].textField.setBackground(errorColor);
                            if(!pc[index].isError){
                            pc[index].isError=true;
                            PairComponent.errorCounter++;
                            }
                        }
                    }
                    if(PairComponent.errorCounter==0){
                        pc[index].isError=false;
                        saveButton.setEnabled(true);
                    }else{
                        saveButton.setEnabled(false);
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(index==0) stockNameLabel.setText(pc[0].textField.getText());
                    if(index>2){
                        if(IsFloat(pc[index].textField.getText())){
                            pc[index].textField.setBackground(pc[index].tfBackColor);
                            if(pc[index].isError){
                                pc[index].isError=false;
                                PairComponent.errorCounter--;
                            }
                        }else{
                            pc[index].textField.setBackground(errorColor);
                            if(!pc[index].isError){
                            pc[index].isError=true;
                            PairComponent.errorCounter++;
                            }
                        }
                    }
                    if(PairComponent.errorCounter==0){
                        pc[index].isError=false;
                        saveButton.setEnabled(true);
                    }else{
                        saveButton.setEnabled(false);
                    }
                }
            });
        }
        
        pc[0].textField.requestFocus();
    }
    void UpdateMutualFundFrame(MyFrame mfFrame,String mfName){
        MyFrame myFrame=new MyFrame();
        JLabel stockNameLabel;
        JButton saveButton,deletButton;
        PairComponent pc[]=new PairComponent[MFComptLabels.length];
        int indexOfMFName=-1;
        myFrame.addTopLabel("Update Mutual Fund");
        myFrame.addBackButton();
        for (ActionListener al : myFrame.backButton.getActionListeners()) {
            myFrame.backButton.removeActionListener(al);
        }
        myFrame.backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                myFrame.dispose();
                PairComponent.currentXPos=5;
                PairComponent.currentYPos=150;
                PairComponent.prevXPos=5;
                PairComponent.prevYPos=150;
                PairComponent.errorCounter=0;
                mfFrame.setVisible(true);
            }
        });
        myFrame.addExitButton();
        myFrame.addWatchListStatusButton();
        mfFrame.setVisible(false);

        stockNameLabel=new JLabel(UnderScoreToSpace(mfName));
        stockNameLabel.setBounds(0,myFrame.fontSize+50,MyFrame.frameWidth,myFrame.fontSize+10);
        stockNameLabel.setOpaque(true);
        stockNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        stockNameLabel.setBackground(Color.BLACK);
        stockNameLabel.setForeground(Color.WHITE);
        stockNameLabel.setFont(new Font("Times New Roman",Font.PLAIN,myFrame.fontSize+5));
        stockNameLabel.setBorder(myFrame.borderColor);
        myFrame.add(stockNameLabel);

        saveButton=new JButton("Save");
        saveButton.setBounds(MyFrame.frameWidth/2-50,MyFrame.frameHeight-myFrame.fontSize-5,100,myFrame.fontSize);
        saveButton.setBackground(myFrame.compBackColor);
        saveButton.setForeground(myFrame.compForeColor);
        saveButton.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        saveButton.setBorder(myFrame.borderColor);
        myFrame.add(saveButton);

        deletButton=new JButton("Delete");
        deletButton.setBounds(MyFrame.frameWidth/2+MyFrame.frameWidth/4-100,MyFrame.frameHeight-myFrame.fontSize-5,100,myFrame.fontSize);
        deletButton.setBackground(myFrame.compBackColor);
        deletButton.setForeground(myFrame.compForeColor);
        deletButton.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        deletButton.setBorder(myFrame.borderColor);
        myFrame.add(deletButton);

        for(int i=0;i<originalInfoList.size();i++){
            if(originalInfoList.get(i).get(0).toString().equals(mfName)){
                indexOfMFName=i;
                break;
            }
        }
        myFrame.isWatchListed=Boolean.valueOf(originalInfoList.get(indexOfMFName).get(originalInfoList.get(indexOfMFName).size()-1).toString());
        if(myFrame.isWatchListed){
            myFrame.watchListStatus.setForeground(myFrame.compForeColor);
        }else{
            myFrame.watchListStatus.setForeground(Color.DARK_GRAY);
        }
        for(int i=0;i<MFComptLabels.length;i++){
            pc[i]=new PairComponent(MFComptLabels[i]);
            pc[i].adjustWidthOnText(MFComptLabels[i]);
            pc[i].textField.setText(UnderScoreToSpace(originalInfoList.get(indexOfMFName).get(i).toString()));
            if(i<2){
                pc[i].setTextFieldWidth(500);
            }
            if(i<MFComptLabels.length-1){
                final int index=i;
                pc[i].textField.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e){
                        pc[index+1].textField.requestFocus();
                    }
                });
            }
            myFrame.addPairComponent(pc[i].label, pc[i].textField);
            
            final int index=i;
            pc[i].textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(index==0) stockNameLabel.setText(pc[0].textField.getText());
                    if(index>2){
                        if(IsFloat(pc[index].textField.getText())){
                            pc[index].textField.setBackground(pc[index].tfBackColor);
                            if(pc[index].isError){
                                pc[index].isError=false;
                                PairComponent.errorCounter--;
                            }
                        }else{
                            pc[index].textField.setBackground(errorColor);
                            if(!pc[index].isError){
                            pc[index].isError=true;
                            PairComponent.errorCounter++;
                            }
                        }
                    }
                    if(PairComponent.errorCounter==0){
                        pc[index].isError=false;
                        saveButton.setEnabled(true);
                    }else{
                        saveButton.setEnabled(false);
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(index==0) stockNameLabel.setText(pc[0].textField.getText());
                    if(index>2){
                        if(IsFloat(pc[index].textField.getText())){
                            pc[index].textField.setBackground(pc[index].tfBackColor);
                            if(pc[index].isError){
                                pc[index].isError=false;
                                PairComponent.errorCounter--;
                            }
                        }else{
                            pc[index].textField.setBackground(errorColor);
                            if(!pc[index].isError){
                            pc[index].isError=true;
                            PairComponent.errorCounter++;
                            }
                        }
                    }
                    if(PairComponent.errorCounter==0){
                        pc[index].isError=false;
                        saveButton.setEnabled(true);
                    }else{
                        saveButton.setEnabled(false);
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(index==0) stockNameLabel.setText(pc[0].textField.getText());
                    if(index>2){
                        if(IsFloat(pc[index].textField.getText())){
                            pc[index].textField.setBackground(pc[index].tfBackColor);
                            if(pc[index].isError){
                                pc[index].isError=false;
                                PairComponent.errorCounter--;
                            }
                        }else{
                            pc[index].textField.setBackground(errorColor);
                            if(!pc[index].isError){
                            pc[index].isError=true;
                            PairComponent.errorCounter++;
                            }
                        }
                    }
                    if(PairComponent.errorCounter==0){
                        pc[index].isError=false;
                        saveButton.setEnabled(true);
                    }else{
                        saveButton.setEnabled(false);
                    }
                }
            });
        }
        final int finalIndexOfMFName=indexOfMFName;
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                UpdateAllInfoList(finalIndexOfMFName,pc,myFrame.isWatchListed);
                WriteToFileByList(mutualfundInfoFile);
            }
        });
        deletButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                RemoveRowFromAllList(finalIndexOfMFName);
                WriteToFileByList(mutualfundInfoFile);
                mfFrame.setVisible(true);
                PairComponent.currentXPos=5;
                PairComponent.currentYPos=150;
                PairComponent.prevXPos=5;
                PairComponent.prevYPos=150;
                PairComponent.errorCounter=0;
                RefreshListPanel("");
                myFrame.dispose();
            }
        });
        pc[0].textField.requestFocus();
    }
    void AddListToPanel(MyFrame myFrame,JPanel listPanel,boolean isForWL,boolean isForStock){
        Color listBackColor=new Color(30,30,30);
        setTempListInfo(isForWL);
        int noOfRows=tempInfoList.size();
        listPanel.setPreferredSize(new Dimension(MyFrame.frameWidth,noOfRows*(myFrame.fontSize+20)));
        nameListButton=new JButton[noOfRows];
        filterListLabel=new JLabel[noOfRows];
        for(int i=0;i<noOfRows;i++){
            nameListButton[i]=new JButton(UnderScoreToSpace(tempInfoList.get(i).get(0).toString()));
            nameListButton[i].setBounds(0,i*(myFrame.fontSize+20),MyFrame.frameWidth/2+300,myFrame.fontSize+5);
            nameListButton[i].setBackground(listBackColor);
            nameListButton[i].setForeground(Color.WHITE);
            nameListButton[i].setHorizontalAlignment(SwingConstants.LEFT);
            nameListButton[i].setFont(new Font("Times New Roman",Font.PLAIN,myFrame.fontSize));
            nameListButton[i].setBorder(myFrame.borderColor);
            listPanel.add(nameListButton[i]);
            final int index=i;
            nameListButton[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    if(index<tempInfoList.size()){
                        if(isForStock){
                            UpdateStockFrame(myFrame, SpaceToUnderScore(nameListButton[index].getText().toString()));
                        }else{
                            UpdateMutualFundFrame(myFrame, SpaceToUnderScore(nameListButton[index].getText().toString()));
                        }
                    }
                }
            });
            
            filterListLabel[i]=new JLabel(UnderScoreToSpace(tempInfoList.get(i).get(selectedFilterIndex).toString()));
            filterListLabel[i].setBounds(MyFrame.frameWidth/2+300,i*(myFrame.fontSize+20),MyFrame.frameWidth/4+50,myFrame.fontSize+5);
            filterListLabel[i].setBackground(listBackColor);
            filterListLabel[i].setForeground(Color.WHITE);
            filterListLabel[i].setHorizontalAlignment(SwingConstants.LEFT);
            filterListLabel[i].setFont(new Font("Times New Roman",Font.PLAIN,myFrame.fontSize));
            filterListLabel[i].setBorder(myFrame.borderColor);
            listPanel.add(filterListLabel[i]);
        }
    }
    void RefreshListPanel(String search){
        tempInfoList.clear();
        for(int i=0;i<infoList.size();i++){
            if(infoList.get(i).get(0).toString().toLowerCase().contains(search.toLowerCase())
            ||infoList.get(i).get(1).toString().toLowerCase().contains(search.toLowerCase())){
                tempInfoList.add(infoList.get(i));
            }
            nameListButton[i].setText(null);
            filterListLabel[i].setText(null);
        }
        for(int i=0;i<tempInfoList.size();i++){
            nameListButton[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(0).toString()));
            filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(selectedFilterIndex).toString()));
        }
    }
    void SearchStockFrame(){
        MyFrame myFrame=new MyFrame();
        JLabel searchLabel;
        JTextField searchField;
        JComboBox<String> comboBox;
        JButton ascButton,descButton;
        JPanel listPanel;
        JScrollPane scrollPane;
        AtomicBoolean isAsc=new AtomicBoolean(true);
        ReadFromFile(stockInfoFile,stockCompLabels.length+1);
        myFrame.addTopLabel("Search Stocks");
        myFrame.addBackButton();
        myFrame.addExitButton();
        
        searchLabel=new JLabel("Search");
        searchLabel.setBounds(myFrame.fontSize,myFrame.fontSize+20,100,myFrame.fontSize+5);
        searchLabel.setBackground(myFrame.compBackColor);
        searchLabel.setForeground(myFrame.compForeColor);
        searchLabel.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        searchLabel.setBorder(myFrame.borderColor);
        myFrame.add(searchLabel);

        searchField=new JTextField();
        searchField.setBounds(myFrame.fontSize+110,myFrame.fontSize+20,MyFrame.frameWidth/2+20,myFrame.fontSize+5);
        searchField.setBackground(myFrame.compBackColor);
        searchField.setForeground(Color.WHITE);
        searchField.setFont(new Font("Times New Roman",Font.PLAIN,myFrame.fontSize));
        searchField.setBorder(myFrame.borderColor);
        myFrame.add(searchField);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                RefreshListPanel(searchField.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                RefreshListPanel(searchField.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                RefreshListPanel(searchField.getText());
            }
        });
        
        comboBox=new JComboBox<>(stockCompLabels);
        comboBox.setBounds(myFrame.fontSize+140+MyFrame.frameWidth/2,myFrame.fontSize+20,420,myFrame.fontSize+5);
        comboBox.setBackground(myFrame.compBackColor);
        comboBox.setForeground(myFrame.compForeColor);
        comboBox.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        comboBox.setBorder(myFrame.borderColor);
        myFrame.add(comboBox);
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                selectedFilterIndex=comboBox.getSelectedIndex();
                for(int i=0;i<tempInfoList.size();i++){
                    if(selectedFilterIndex==0)
                    filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(3).toString()));
                    else
                    filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(selectedFilterIndex).toString()));
                }
            }
        });

        
        ascButton=new JButton("\u25B2");
        ascButton.setBounds(myFrame.fontSize+580+MyFrame.frameWidth/2,myFrame.fontSize+20,50,myFrame.fontSize+5);
        ascButton.setBackground(myFrame.compBackColor);
        ascButton.setForeground(myFrame.compForeColor);
        //ToolTipManager.sharedInstance().setInitialDelay(500);
        // ascButton.setToolTipText("This button provides more info!");
        // ascButton.setToolTipText("<html><body style='background-color:black; color:white; font-size:12px;'>This is a custom tooltip!</body></html>");
        ascButton.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        ascButton.setBorder(myFrame.borderColor);
        myFrame.add(ascButton);
        
        
        descButton=new JButton("\u25BC");
        descButton.setBounds(myFrame.fontSize+640+MyFrame.frameWidth/2,myFrame.fontSize+20,50,myFrame.fontSize+5);
        descButton.setBackground(myFrame.compBackColor);
        descButton.setForeground(Color.DARK_GRAY);
        descButton.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        descButton.setBorder(myFrame.borderColor);
        myFrame.add(descButton);
        
        ascButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                isAsc.set(true);
                ascButton.setForeground(myFrame.compForeColor);
                descButton.setForeground(Color.DARK_GRAY);
                Sort(true);
                for(int i=0;i<tempInfoList.size();i++){
                    nameListButton[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(0).toString()));
                    filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(selectedFilterIndex).toString()));
                }
            }
        });
        descButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                isAsc.set(false);
                descButton.setForeground(myFrame.compForeColor);
                ascButton.setForeground(Color.DARK_GRAY);
                Sort(false);
                for(int i=0;i<tempInfoList.size();i++){
                    nameListButton[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(0).toString()));
                    filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(selectedFilterIndex).toString()));
                }
            }
        });
        
        
        listPanel=new JPanel();
        listPanel.setLayout(null);
        AddListToPanel(myFrame,listPanel,false,true);
        listPanel.setBackground(myFrame.compBackColor);
        listPanel.setForeground(myFrame.compForeColor);
        listPanel.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        listPanel.setBorder(myFrame.borderColor);
        
        
        
        scrollPane=new JScrollPane(listPanel);
        scrollPane.setBounds(0,(myFrame.fontSize+5)*2+25,MyFrame.frameWidth,MyFrame.frameHeight-((myFrame.fontSize+5)*3+25));
        scrollPane.setBackground(myFrame.compBackColor);
        scrollPane.setForeground(myFrame.compForeColor);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        scrollPane.setBorder(myFrame.borderColor);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI(){
            public void configureScrollBarColors(){
                thumbColor=myFrame.compForeColor;
                trackColor=myFrame.compBackColor;
            }
        });
        myFrame.add(scrollPane);
    }
    void SearchMutualFundFrame(){
        MyFrame myFrame=new MyFrame();
        JLabel searchLabel;
        JTextField searchField;
        JComboBox<String> comboBox;
        JButton ascButton,descButton;
        JPanel listPanel;
        JScrollPane scrollPane;
        AtomicBoolean isAsc=new AtomicBoolean(true);
        ReadFromFile(mutualfundInfoFile,MFComptLabels.length+1);
        myFrame.addTopLabel("Search Mutual Funds");
        myFrame.addBackButton();
        myFrame.addExitButton();
        
        searchLabel=new JLabel("Search");
        searchLabel.setBounds(myFrame.fontSize,myFrame.fontSize+20,100,myFrame.fontSize+5);
        searchLabel.setBackground(myFrame.compBackColor);
        searchLabel.setForeground(myFrame.compForeColor);
        searchLabel.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        searchLabel.setBorder(myFrame.borderColor);
        myFrame.add(searchLabel);
            
        searchField=new JTextField();
        searchField.setBounds(myFrame.fontSize+110,myFrame.fontSize+20,MyFrame.frameWidth/2+20,myFrame.fontSize+5);
        searchField.setBackground(myFrame.compBackColor);
        searchField.setForeground(Color.WHITE);
        searchField.setFont(new Font("Times New Roman",Font.PLAIN,myFrame.fontSize));
        searchField.setBorder(myFrame.borderColor);
        myFrame.add(searchField);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                RefreshListPanel(searchField.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                RefreshListPanel(searchField.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                RefreshListPanel(searchField.getText());
            }
        });

        comboBox=new JComboBox<>(MFComptLabels);
        comboBox.setBounds(myFrame.fontSize+140+MyFrame.frameWidth/2,myFrame.fontSize+20,420,myFrame.fontSize+5);
        comboBox.setBackground(myFrame.compBackColor);
        comboBox.setForeground(myFrame.compForeColor);
        comboBox.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        comboBox.setBorder(myFrame.borderColor);
        myFrame.add(comboBox);
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                selectedFilterIndex=comboBox.getSelectedIndex();
                for(int i=0;i<tempInfoList.size();i++){
                    if(selectedFilterIndex==0)
                    filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(3).toString()));
                    else
                    filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(selectedFilterIndex).toString()));
                }
            }
        });
        
        ascButton=new JButton("\u25B2");
        ascButton.setBounds(myFrame.fontSize+580+MyFrame.frameWidth/2,myFrame.fontSize+20,50,myFrame.fontSize+5);
        ascButton.setBackground(myFrame.compBackColor);
        ascButton.setForeground(myFrame.compForeColor);
        //ToolTipManager.sharedInstance().setInitialDelay(500);
        // ascButton.setToolTipText("This button provides more info!");
        // ascButton.setToolTipText("<html><body style='background-color:black; color:white; font-size:12px;'>This is a custom tooltip!</body></html>");
        ascButton.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        ascButton.setBorder(myFrame.borderColor);
        myFrame.add(ascButton);
        
        
        descButton=new JButton("\u25BC");
        descButton.setBounds(myFrame.fontSize+640+MyFrame.frameWidth/2,myFrame.fontSize+20,50,myFrame.fontSize+5);
        descButton.setBackground(myFrame.compBackColor);
        descButton.setForeground(Color.DARK_GRAY);
        descButton.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        descButton.setBorder(myFrame.borderColor);
        myFrame.add(descButton);
        
        ascButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                isAsc.set(true);
                ascButton.setForeground(myFrame.compForeColor);
                descButton.setForeground(Color.DARK_GRAY);
                Sort(true);
                for(int i=0;i<tempInfoList.size();i++){
                    nameListButton[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(0).toString()));
                    filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(selectedFilterIndex).toString()));
                }
            }
        });
        descButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                isAsc.set(false);
                descButton.setForeground(myFrame.compForeColor);
                ascButton.setForeground(Color.DARK_GRAY);
                Sort(false);
                for(int i=0;i<tempInfoList.size();i++){
                    nameListButton[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(0).toString()));
                    filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(selectedFilterIndex).toString()));
                }
            }
        });

        
        listPanel=new JPanel();
        listPanel.setLayout(null);
        AddListToPanel(myFrame,listPanel,false,false);
        listPanel.setBackground(myFrame.compBackColor);
        listPanel.setForeground(myFrame.compForeColor);
        listPanel.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        listPanel.setBorder(myFrame.borderColor);
        
        
        
        scrollPane=new JScrollPane(listPanel);
        scrollPane.setBounds(0,(myFrame.fontSize+5)*2+25,MyFrame.frameWidth,MyFrame.frameHeight-((myFrame.fontSize+5)*3+25));
        scrollPane.setBackground(myFrame.compBackColor);
        scrollPane.setForeground(myFrame.compForeColor);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        scrollPane.setBorder(myFrame.borderColor);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI(){
            public void configureScrollBarColors(){
                thumbColor=myFrame.compForeColor;
                trackColor=myFrame.compBackColor;
            }
        });
        myFrame.add(scrollPane);
    }
    void StockWatchListFrame(){
        MyFrame myFrame=new MyFrame();
        JLabel searchLabel;
        JTextField searchField;
        JComboBox<String> comboBox;
        JButton ascButton,descButton;
        JPanel listPanel;
        JScrollPane scrollPane;
        AtomicBoolean isAsc=new AtomicBoolean(true);
        ReadFromFile(stockInfoFile,stockCompLabels.length+1);

        myFrame.addTopLabel("Stocks WatchList");
        myFrame.addBackButton();
        myFrame.addExitButton();

        searchLabel=new JLabel("Search");
        searchLabel.setBounds(myFrame.fontSize,myFrame.fontSize+20,100,myFrame.fontSize+5);
        searchLabel.setBackground(myFrame.compBackColor);
        searchLabel.setForeground(myFrame.compForeColor);
        searchLabel.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        searchLabel.setBorder(myFrame.borderColor);
        myFrame.add(searchLabel);

        searchField=new JTextField();
        searchField.setBounds(myFrame.fontSize+110,myFrame.fontSize+20,MyFrame.frameWidth/2+20,myFrame.fontSize+5);
        searchField.setBackground(myFrame.compBackColor);
        searchField.setForeground(Color.WHITE);
        searchField.setFont(new Font("Times New Roman",Font.PLAIN,myFrame.fontSize));
        searchField.setBorder(myFrame.borderColor);
        myFrame.add(searchField);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                RefreshListPanel(searchField.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                RefreshListPanel(searchField.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                RefreshListPanel(searchField.getText());
            }
        });
        
        comboBox=new JComboBox<>(stockCompLabels);
        comboBox.setBounds(myFrame.fontSize+140+MyFrame.frameWidth/2,myFrame.fontSize+20,420,myFrame.fontSize+5);
        comboBox.setBackground(myFrame.compBackColor);
        comboBox.setForeground(myFrame.compForeColor);
        comboBox.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        comboBox.setBorder(myFrame.borderColor);
        myFrame.add(comboBox);
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                selectedFilterIndex=comboBox.getSelectedIndex();
                for(int i=0;i<tempInfoList.size();i++){
                    if(selectedFilterIndex==0)
                    filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(3).toString()));
                    else
                    filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(selectedFilterIndex).toString()));
                }
            }
        });

        ascButton=new JButton("\u25B2");
        ascButton.setBounds(myFrame.fontSize+580+MyFrame.frameWidth/2,myFrame.fontSize+20,50,myFrame.fontSize+5);
        ascButton.setBackground(myFrame.compBackColor);
        ascButton.setForeground(myFrame.compForeColor);
        //ToolTipManager.sharedInstance().setInitialDelay(500);
        // ascButton.setToolTipText("This button provides more info!");
        // ascButton.setToolTipText("<html><body style='background-color:black; color:white; font-size:12px;'>This is a custom tooltip!</body></html>");
        ascButton.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        ascButton.setBorder(myFrame.borderColor);
        myFrame.add(ascButton);
        
        
        descButton=new JButton("\u25BC");
        descButton.setBounds(myFrame.fontSize+640+MyFrame.frameWidth/2,myFrame.fontSize+20,50,myFrame.fontSize+5);
        descButton.setBackground(myFrame.compBackColor);
        descButton.setForeground(Color.DARK_GRAY);
        descButton.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        descButton.setBorder(myFrame.borderColor);
        myFrame.add(descButton);
        
        ascButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                isAsc.set(true);
                ascButton.setForeground(myFrame.compForeColor);
                descButton.setForeground(Color.DARK_GRAY);
                Sort(true);
                for(int i=0;i<tempInfoList.size();i++){
                    nameListButton[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(0).toString()));
                    filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(selectedFilterIndex).toString()));
                }
            }
        });
        descButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                isAsc.set(false);
                descButton.setForeground(myFrame.compForeColor);
                ascButton.setForeground(Color.DARK_GRAY);
                Sort(false);
                for(int i=0;i<tempInfoList.size();i++){
                    nameListButton[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(0).toString()));
                    filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(selectedFilterIndex).toString()));
                }
            }
        });


        listPanel=new JPanel();
        listPanel.setLayout(null);
        AddListToPanel(myFrame,listPanel,true,true);
        listPanel.setBackground(myFrame.compBackColor);
        listPanel.setForeground(myFrame.compForeColor);
        listPanel.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        listPanel.setBorder(myFrame.borderColor);
        
        
        
        scrollPane=new JScrollPane(listPanel);
        scrollPane.setBounds(0,(myFrame.fontSize+5)*2+25,MyFrame.frameWidth,MyFrame.frameHeight-((myFrame.fontSize+5)*3+25));
        scrollPane.setBackground(myFrame.compBackColor);
        scrollPane.setForeground(myFrame.compForeColor);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        scrollPane.setBorder(myFrame.borderColor);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI(){
            public void configureScrollBarColors(){
                thumbColor=myFrame.compForeColor;
                trackColor=myFrame.compBackColor;
            }
        });
        myFrame.add(scrollPane);
    }
    void MutualFundWatchListFrame(){
        MyFrame myFrame=new MyFrame();
        JLabel searchLabel;
        JTextField searchField;
        JComboBox<String> comboBox;
        JButton ascButton,descButton;
        JPanel listPanel;
        JScrollPane scrollPane;
        AtomicBoolean isAsc=new AtomicBoolean(true);
        ReadFromFile(mutualfundInfoFile,MFComptLabels.length+1);
        myFrame.addTopLabel("Mutual Fund WatchList");
        myFrame.addBackButton();
        myFrame.addExitButton();

        searchLabel=new JLabel("Search");
        searchLabel.setBounds(myFrame.fontSize,myFrame.fontSize+20,100,myFrame.fontSize+5);
        searchLabel.setBackground(myFrame.compBackColor);
        searchLabel.setForeground(myFrame.compForeColor);
        searchLabel.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        searchLabel.setBorder(myFrame.borderColor);
        myFrame.add(searchLabel);

        searchField=new JTextField();
        searchField.setBounds(myFrame.fontSize+110,myFrame.fontSize+20,MyFrame.frameWidth/2+20,myFrame.fontSize+5);
        searchField.setBackground(myFrame.compBackColor);
        searchField.setForeground(Color.WHITE);
        searchField.setFont(new Font("Times New Roman",Font.PLAIN,myFrame.fontSize));
        searchField.setBorder(myFrame.borderColor);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                RefreshListPanel(searchField.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                RefreshListPanel(searchField.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                RefreshListPanel(searchField.getText());
            }
        });
        myFrame.add(searchField);
        
        comboBox=new JComboBox<>(MFComptLabels);
        comboBox.setBounds(myFrame.fontSize+140+MyFrame.frameWidth/2,myFrame.fontSize+20,420,myFrame.fontSize+5);
        comboBox.setBackground(myFrame.compBackColor);
        comboBox.setForeground(myFrame.compForeColor);
        comboBox.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        comboBox.setBorder(myFrame.borderColor);
        myFrame.add(comboBox);
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                selectedFilterIndex=comboBox.getSelectedIndex();
                for(int i=0;i<tempInfoList.size();i++){
                    if(selectedFilterIndex==0)
                    filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(3).toString()));
                    else
                    filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(selectedFilterIndex).toString()));
                }
            }
        });

        ascButton=new JButton("\u25B2");
        ascButton.setBounds(myFrame.fontSize+580+MyFrame.frameWidth/2,myFrame.fontSize+20,50,myFrame.fontSize+5);
        ascButton.setBackground(myFrame.compBackColor);
        ascButton.setForeground(myFrame.compForeColor);
        ascButton.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        ascButton.setBorder(myFrame.borderColor);
        myFrame.add(ascButton);
        
        
        descButton=new JButton("\u25BC");
        descButton.setBounds(myFrame.fontSize+640+MyFrame.frameWidth/2,myFrame.fontSize+20,50,myFrame.fontSize+5);
        descButton.setBackground(myFrame.compBackColor);
        descButton.setForeground(Color.DARK_GRAY);
        descButton.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        descButton.setBorder(myFrame.borderColor);
        myFrame.add(descButton);
        
        ascButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                isAsc.set(true);
                ascButton.setForeground(myFrame.compForeColor);
                descButton.setForeground(Color.DARK_GRAY);
                Sort(true);
                for(int i=0;i<tempInfoList.size();i++){
                    nameListButton[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(0).toString()));
                    filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(selectedFilterIndex).toString()));
                }
            }
        });
        descButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                isAsc.set(false);
                descButton.setForeground(myFrame.compForeColor);
                ascButton.setForeground(Color.DARK_GRAY);
                Sort(false);
                for(int i=0;i<tempInfoList.size();i++){
                    nameListButton[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(0).toString()));
                    filterListLabel[i].setText(UnderScoreToSpace(tempInfoList.get(i).get(selectedFilterIndex).toString()));
                }
            }
        });


        listPanel=new JPanel();
        listPanel.setLayout(null);
        AddListToPanel(myFrame,listPanel,true,false);
        listPanel.setBackground(myFrame.compBackColor);
        listPanel.setForeground(myFrame.compForeColor);
        listPanel.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        listPanel.setBorder(myFrame.borderColor);
        
        
        
        scrollPane=new JScrollPane(listPanel);
        scrollPane.setBounds(0,(myFrame.fontSize+5)*2+25,MyFrame.frameWidth,MyFrame.frameHeight-((myFrame.fontSize+5)*3+25));
        scrollPane.setBackground(myFrame.compBackColor);
        scrollPane.setForeground(myFrame.compForeColor);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setFont(new Font("Times New Roman",Font.BOLD,myFrame.fontSize));
        scrollPane.setBorder(myFrame.borderColor);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI(){
            public void configureScrollBarColors(){
                thumbColor=myFrame.compForeColor;
                trackColor=myFrame.compBackColor;
            }
        });
        myFrame.add(scrollPane);
    }
    public static void main(String[] args) {
        StockScreener ssobj=new StockScreener();
        ssobj.MainFrame();
    }
}