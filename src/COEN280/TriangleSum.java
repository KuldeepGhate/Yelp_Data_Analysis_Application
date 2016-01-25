package COEN280;

/**
 * Created by Kuldeep on 11/25/2015.
 */
public class TriangleSum {
    public static void main(String[] args){
        int[] input = {5,6,9,4,6,8,0,7,1,5};

        //check if valid
        int checkValid = 1 + (8* input.length);
        if(Math.sqrt(checkValid)== (int)Math.sqrt(checkValid)){
            int count = 2;
            int sum = 0;
            int end = 1, i=0,max = input[0], start = 0;
            while(end <= input.length){
                if(i == end){
                    start = end;
                    end += count;
                    count++;
                    sum += max;
                    if(start != input.length)
                    max = input[start];
                    System.out.println("max: "+ max);

                }
                else{
                    if(input[i] > max){
                        max = input[i];
     //                   System.out.println("max: "+ max);
                    }
                    i++;
                }

            }
            System.out.println("Sum = " +sum);
        }
        else{
            System.out.println("Invalid input");
        }
    }
}
