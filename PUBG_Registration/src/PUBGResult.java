import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.awt.Graphics;
class PUBGResult{
   Image backgroundImage = Toolkit.getDefaultToolkit().getImage("Background.jpg");
   Frame resultFrame = new Frame(){
      public void paint(Graphics g) {
         g.drawImage(backgroundImage, 0, 0,getWidth(),getHeight(),this);
      }
   };
   Label[][] resultLabel;
   int round;
   int teamNameCount = 21;
   int[][] points;
   int resultRank = 1;
   int teamNumber = 0;
   int k = 0;
   int[] teamPoints;
   int size = 100;
   int resetButtonCount = 0;
   String[][] teamNameArray;
   Button resetAll = new Button("Reset All");
   Button backButton = new Button("Back");
   Color[] colorArray = new Color[5];
   boolean bool = true;
   PUBGResult() {
      teamNumber = teamNameCount - 1;
      resultLabel = new Label[teamNumber][6];
      GetColor();
   }
   public void GetColor() {
      try {
         File file = new File("Color.txt");
         Scanner sc = new Scanner(file);

         String str;
         int i;
         for(i = 0; i < 7; ++i) {
            str = sc.nextLine();
         }

         for(i = 0; sc.hasNextLine(); ++i) {
            str = sc.next();
            int R = sc.nextInt();
            int G = sc.nextInt();
            int B = sc.nextInt();
            colorArray[i] = new Color(R, G, B);
         }
         sc.close();
      } catch (Exception e) {
      }

   }
   public void start() {
      Round();
      TeamCount();
      teamNumber = teamNameCount - 1;
      resultLabel = new Label[teamNumber][6];
   }
   public void Round() {
      File file = new File("Round.txt");
      try {
         Scanner sc = new Scanner(file);
         round = sc.nextInt();
         sc.close();
      } catch (Exception e) {
      }

   }
   public void TeamCount() {
      File file = new File("TeamCount.txt");
      try {
         Scanner sc = new Scanner(file);
         teamNameCount = sc.nextInt();
         sc.close();
      } catch (Exception e) {
      }

   }
   public void ResultFrame() {
      resultFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
      if (bool) {
         resultFrame.setUndecorated(true);
      }
      resultFrame.setLayout(null);
      resultFrame.setVisible(true);
      resultFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            resultFrame.dispose();
         }
      });
      for(int i = 0; i < teamNumber; ++i) {
         for(int j = 0; j < 6; ++j) {
            backButton.setBounds(0, 0, 100, 50);
            backButton.setFont(new Font("", Font.PLAIN, 40));
            backButton.setBackground(Color.LIGHT_GRAY);
            resultFrame.add(backButton);
            resultLabel[i][j] = new Label();
            if (teamNumber <= 10) {
               resultLabel[i][j].setBounds(i * 310 - k * 1550, j * 60 + 60 + k * 400, 300, 50);
               if (j < 2) {
                  resultLabel[i][j].setFont(new Font("", Font.BOLD, 40));
               } else {
                  resultLabel[i][j].setFont(new Font("", Font.PLAIN, 40));
               }
            } else {
               resultLabel[i][j].setBounds(i * 155 - k * 1550, j * 40 + 80 + k * 400, 150, 30);
               if (j < 2) {
                  resultLabel[i][j].setFont(new Font("", Font.BOLD, 20));
               } else {
                  resultLabel[i][j].setFont(new Font("", Font.PLAIN, 20));
               }
            }
            if (i == 0) {
               resultLabel[i][j].setBackground(colorArray[0]);
            } else if (i == 1) {
               resultLabel[i][j].setBackground(colorArray[1]);
            } else if (i == 2) {
               resultLabel[i][j].setBackground(colorArray[2]);
            } else {
               resultLabel[i][j].setBackground(colorArray[3]);
            }
            resultFrame.add(resultLabel[i][j]);
         }
         if (teamNumber <= 10 && i == 4) {
            ++k;
         } else if (teamNumber > 10 && i == 9) {
            ++k;
         }
      }
      backButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            resultFrame.dispose();
         }
      });
   }
   public void Result() {
      ResultFrame();
      for(int i = 0; i < teamNumber; ++i) {
         for(int j = 0; j < 6; ++j) {
            if (j == 0) {
               resultLabel[i][j].setText(String.valueOf(resultRank) + ".Points:" + teamPoints[resultRank++ - 1]);
               resultLabel[i][j].setAlignment(Label.CENTER);
            } else {
               resultLabel[i][j].setText(FilterName(teamNameArray[i][j - 1], j - 1));
            }
         }
      }
   }
   public String FilterName(String str, int n) {
      int indexOfColumn = str.indexOf(":");
      if (n == 0) {
         str = str.substring(indexOfColumn + 1, str.indexOf(":", indexOfColumn + 1));
      } else {
         str = str.substring(indexOfColumn + 1);
      }
      return str;
   }
   public void FinalResult() {
      ResultFrame();
      if (teamNameCount < 12) {
         resetAll.setBounds(12 * size + size / 2, 4 * size + 10, 2 * size + 20, size / 2);
      } else {
         resetAll.setBounds(12 * size + size / 2, 7 * size + size / 2, 2 * size + 20, size / 2);
      }

      resetAll.setFont(new Font("", Font.PLAIN, size / 2));
      resetAll.setBackground(colorArray[4]);
      resultFrame.add(resetAll);
      for(int i = 0; i < teamNumber; ++i) {
         for(int j = 0; j < 6; ++j) {
            if (j == 0) {
               resultLabel[i][j].setText(String.valueOf(resultRank) + ".Points:" + teamPoints[resultRank++ - 1]);
               resultLabel[i][j].setAlignment(1);
            } else {
               resultLabel[i][j].setText(FilterName(teamNameArray[i][j - 1], j - 1));
            }
         }
      }

      resetAll.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ++resetButtonCount;
            if (resetButtonCount > 2) {
               resultFrame.dispose();
            }
            ResetAll();
         }
      });
   }
   public void ResetAll() {
      try {
         FileWriter f1 = new FileWriter("TeamCount.txt");
         FileWriter f2 = new FileWriter("TeamName.txt");
         FileWriter f3 = new FileWriter("Round.txt");
         f1.write("1");
         f2.write("");
         f3.write("0");
         f1.close();
         f2.close();
         f3.close();
      } catch (Exception e) {
      }

   }
   public void GetTeamName() {
      teamNameArray = new String[teamNameCount - 1][5];

      try {
         File file = new File("TeamName.txt");
         Scanner sc = new Scanner(file);

         for(int i = 0; i < teamNameCount - 1; ++i) {
            for(int j = 0; j < 5; ++j) {
               teamNameArray[i][j] = sc.nextLine();
            }
         }
      } catch (Exception e) {
      }

   }
   public int FilterPoints(String str) {
      int count = 0;
      for(int i = round - 1; i > 0; --i) {
         str = str.substring(0, str.lastIndexOf(":"));
         int intPoints = Integer.parseInt(str.substring(str.lastIndexOf(":") + 1));
         str = str.substring(0, str.indexOf("Point" + String.valueOf(i)));
         count += intPoints;
      }
      return count;
   }
   public void Sort() {
      teamPoints = new int[teamNameCount - 1];
      int temp;
      for(temp = 0; temp < teamNameCount - 1; ++temp) {
         teamPoints[temp] = FilterPoints(teamNameArray[temp][0]);
      }
      for(int i = 0; i < teamPoints.length - 1; ++i) {
         for(int j = 0; j < teamPoints.length - i - 1; ++j) {
            if (teamPoints[j] < teamPoints[j + 1]) {
               temp = teamPoints[j];
               teamPoints[j] = teamPoints[j + 1];
               teamPoints[j + 1] = temp;

               for(int k = 0; k < 5; ++k) {
                  String names = teamNameArray[j][k];
                  teamNameArray[j][k] = teamNameArray[j + 1][k];
                  teamNameArray[j + 1][k] = names;
               }
            }
         }
      }
   }
   
}