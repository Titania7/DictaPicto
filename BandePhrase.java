package com.example.dictapicto;

public class BandePhrase {
    private String[][] array;
    private int count;
    private int sizeofarray;

    public BandePhrase(){
        array = new String[1][2];
        count = 0;
        sizeofarray = 1;
    }

    public String[][] getString(){
        return array;
    }
    public int getCount(){
        return count;
    }
    public int getSize(){
        return sizeofarray;
    }


    private void growSize(){   //creates an array of double size

        String temp[][] = null;
        if (count == sizeofarray){
            temp = new String[sizeofarray * 2][2];   //initialize a double size array of array
        }

        for (int i = 0; i < sizeofarray; i++){
            temp[i][0] = array[i][0];
            temp[i][1] = array[i][1];//copies all the elements of the previous array
        }

        array = temp;
        sizeofarray= sizeofarray * 2;
    }

    private void addElementAt(int index, String a, String b){   //adds an element at the specified index

        if (count == sizeofarray){   //compare the size with the number of elements
            growSize();   // grows the array size
        }

        for (int i = count - 1; i >= index; i--){
            array[i + 1][0] = array[i][0];   //shifting all the elements to the left from the specified index
            array[i + 1][1] = array[i][1];
        }

        array[index][0] = a;   //inserts an element at the specified index
        array[index][1] = b;
        count++;
    }



    public void appendEl(String a, String b){   //appends an element at the end of the array

        if (getCount() == getSize()){   //compares if the number of elements is equal to the size of the array or not
            growSize();   //creates an array of double size
        }

        array[count][0] = a;   //appends an element at the end of the array
        array[count][1] = b;
        count++;
    }

    public void delElement(int index){

        for (int i = index; i < count - 1; i++){
            array[i][0] = array[i+1][0];   //shifting all the elements to the left from the specified index
            array[i][1] = array[i+1][1];
        }
        count--;
    }

    public void deplElement(int initpos, int finalpos){

        String[] temp = new String[2];
        temp[0] = array[initpos][0];
        temp[1] = array[initpos][1];

        delElement(initpos);
        addElementAt(finalpos, temp[0], temp[1]);
    }
}
