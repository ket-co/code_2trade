#include<stdio.h>
#include<stdlib.h>
#include<time.h>
#include<ctype.h>
void board(int a[]);
int condition(int a[]);
int position(int a[]);
void delet(int n);
int four();
int checkplayer(int a[], int store[], int count);
int checkbot(int a[], int store[], int count);
void echo();
int countFour=4;
int arrFour[]={1,3,7,9};
int main(){
    int a[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, count = 0, v, store[100], i;
    char name[100];
    printf("Welcome to Tic-Tac-Toe game\nEnter your name\n");
    scanf("%[^\n]d",name);
    board(a);
    while (condition(a) != 0){
        if (count % 2 == 0){
            one:
            printf("It's \e[32m%s\e[0m's turn...\n", name);
            if(!scanf("%d",&v)){ 
                printf("Invalid Input\n");
                return 0;
            }
            if(v<0||v>9){
                printf("Enter the valid value\n");
                goto one;
            }
            for (i = 0; i < count; i++){
                if (store[i] == v){
                    printf("This position is already taken\n");
                    goto one;
                }
            }
            if(count==8){
                a[v]=111;
                board(a);
                printf("Draw\n");
                return 0;
            }
            store[count] = v;
            a[v] = 111;
            count++;
            board(a);
        }
        else{
            two:
            printf("It's \e[33mcomputer\e[0m's turn...\n");
            if (count == 1 && v == 5) v = four();
            else if (count == 1 && v != 5) v = 5;
            if(count==3){
                if(a[2]==a[4]||a[2]==a[6]||a[4]==a[8]||a[6]==a[8]||a[1]==a[5]||a[3]==a[5]||a[7]==a[5]||a[9]==a[5]) v=four();
            }
            if (count>=3){
                if (checkbot(a, store, count) != 0) v = checkbot(a, store, count);
                else if (checkplayer(a, store, count) != 0) v = checkplayer(a, store, count);
                else{
                    if (checkplayer(a, store, count) == 0 && checkbot(a, store, count) == 0){
                        yo:
                        srand(time(0));
                        v = (rand() % 9) + 1;
                        for (i = 0; i < count; i++){
                            if (store[i] == v) goto yo;   
                        }
                    }
                }
            }
        }
        for (i = 0; i < count; i++){
            if (store[i] == v) goto two;
  
        }
        store[count] = v;
        a[v] = 222;
        count++;
        board(a);
        if(count==9){
            printf("It's Draw\n");
            return 0;
        }
        if (condition(a) == 0){
            if (position(a) == 111){
                printf("Congratulation\n%s won the game\n", name);
            }
            else if (position(a) == 222){
                printf("Computer Won the game\n");
            }
        }
    }
}
void board(int a[]){
    int i, j, k = 1;
    system("cls"); //Foe Ubuntu/Termux
    //system("clear"); //For Windows
    echo();
    for (i = 0; i < 3; i++){
        for (j = k; j < k + 3; j++){
            if (a[j] == 111) printf(" \e[32mX\e[0m ");
            else if (a[j] == 222) printf(" \e[33mO\e[0m ");
            else printf(" %d ", a[j]);
        }
        k += 3;
        printf("\n\n");
    }
}
int condition(int a[]){
    int i = 0, flag = 0;
    for (i = 1; i < 8; i += 3){
        if (a[i] == a[i + 1] && a[i] == a[i + 2]) flag = 1;
    }
    for (i = 1; i < 4; i++){
        if (a[i] == a[i + 3] && a[i] == a[i + 6]) flag = 1;
    }
    if (a[1] == a[5] && a[1] == a[9]) flag = 1;
    if (a[3] == a[5] && a[3] == a[7]) flag = 1;
    if (flag == 1) return 0;
    else return 1;
}
int position(int a[]){
    int i = 0, flag = 0;
    for (i = 1; i < 8; i += 3){
        if (a[i] == a[i + 1] && a[i] == a[i + 2]) flag = a[i];
    }
    for (i = 1; i < 4; i++){
        if (a[i] == a[i + 3] && a[i] == a[i + 6]) flag = a[i];
    }
    if (a[1] == a[5] && a[1] == a[9]) flag = a[1];
    if (a[3] == a[5] && a[3] == a[7]) flag = a[3];
    if (flag != 0) return flag;
    else return 0;
}
void delet(int n){
    for(int i=0;i<countFour;i++){
        if(n==arrFour[i]){
            for(int j=i;j<countFour;j++){
                arrFour[j]=arrFour[j+1];
            }
            countFour--;
            return;
        }
    }
}
int four(){
    int i;
    srand(time(0));
    i=(rand()%countFour);
    i=arrFour[i];
    delet(i);
    return i;

}
int checkplayer(int a[], int store[], int count){
    int i, arr[100], j, g = 0, h;
    for (i = 1; i < 10; i++){
        for (j = 0; j < count; j++){
            if (store[j] == i) goto ant;
        }
        arr[g] = i;
        g++;
        ant:
        h = 1;
    }
    for (i = 0; i < 10 - count - 1; i++){
        a[arr[i]] = 111;
        if (condition(a) == 0){
            a[arr[i]] = arr[i];
            return arr[i];
        }
        a[arr[i]] = arr[i];
    }
    return 0;
}
int checkbot(int a[], int store[], int count){
    int i, arr[100], j, g = 0, h;
    for (i = 1; i < 10; i++){
        for (j = 0; j < count; j++){
            if (store[j] == i) goto ant;
        }
        arr[g] = i;
        g++;
        ant:
        h = 1;
    }
    for (i = 0; i < 10 - count - 1; i++){
        a[arr[i]] = 222;
        if (condition(a) == 0){
            a[arr[i]] = arr[i];
            return arr[i];
        }
        a[arr[i]] = arr[i];
    }
    return 0;
}
void echo(){
    char e[] = "Rfij%G~%Htij7Ywfij";
    printf("\e[31mUnbeatable Bot\n");
    for (int i = 0; e[i]; i++){
        printf("\e[36m%c", e[i] - 5);
    }
    printf("\e[0m\n\n");
}