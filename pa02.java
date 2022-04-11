
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



public class pa02 {

    // class attributes
    static int Check_Sum_Size;
    String text;

    // class constructor
    public pa02(){
        Check_Sum_Size=0;
        text = null;
    }
    public static void main(String []args) throws IOException {
        // make the object of the class
        pa02 obj = new pa02();

        // make the path to the file
        Path file = Paths.get(args[0]);
        // read all the bytes of the text file
        obj.text = new String(Files.readAllBytes(file));

        // read the num bits
        obj.Check_Sum_Size = Integer.parseInt(args[1]);

        // exit the program if bit is not valid
        if(obj.Check_Sum_Size != 8 && obj.Check_Sum_Size !=16 && obj.Check_Sum_Size !=32) {
            System.err.println("\nValid checksum sizes are 8, 16, or 32\n");
            System.exit(1);
        }
        // adjust the array and add the padding
        byte[] new_arr = make_array(obj.text, Check_Sum_Size);
        int checksum=0;
        // calculate the 8 bit checksum
        if(Check_Sum_Size == 8)
            checksum = check_sum_8bit(new_arr);
        // calculate the 16 bit checksum
        else if(Check_Sum_Size == 16)
            checksum=check_sum_16bit(new_arr);
        // calculate the 32 bit checksum
        else
            checksum=check_sum_32bit(new_arr);

        // printing out the result
        System.out.printf("\n%s\n%2d bit checksum is %8x for all %4d chars\n",
                adjusted_output(add_Padding(obj.text, Check_Sum_Size)), Check_Sum_Size, checksum, new_arr.length);


    }
    public static byte[] make_array ( String text, int num_bit){
        // save the length of the text file
        int size_arr = text.getBytes().length;
        // calculate the number of padding
        int num_padd = padd(size_arr, num_bit);
        // make a new temp array to save everything together
        byte [] tmp_arr = new byte[size_arr + num_padd];
        int i=0;
        // copy everything from the text to the array
        for(i=0; i<size_arr; i++){
            tmp_arr[i] = (byte) text.charAt(i);
        }
        int j=0;
        // add the padding if needed
        if(num_padd != 0 ){
            for(j=size_arr; j < size_arr+num_padd; j++)
            {
                tmp_arr[j]= 88;
            }
        }
        return tmp_arr;
    }
    // calculate the checksum for 8 bit
    public static int check_sum_8bit(byte[] text) {
        int res = 0;
        for (byte i : text) {
            res += i;
        }
        res = res & 0xFF;
        return res ;
    }
    // calculate the checksum for 16 bit
    public static int check_sum_16bit(byte[] text) {
        int res = 0;
        for (int i = 0; i <= text.length - 2; i += 2) {
            res += ((text[i] << 8) | (text[i + 1] & 0xFF));
        }
        res = res & 0xFFFF;
        return res ;
    }
    // calculate the checksum for 32 bit
    public static int check_sum_32bit(byte[] text) {
        int res = 0;
        for (int i = 0; i < text.length; i += 4) {
            res += ((text[i] << 24) | (text[i + 1] << 16) | (text[i + 2] << 8) | (text[i + 3])) & 0xffffffffL;
        }
        return res;
    }
    public static String adjusted_output(String output) {

        // make a object of new String builder
        StringBuilder new_text = new StringBuilder();
        for (int i = 0; i < output.length(); i++) {
            // put 80 characters in each line
            if (i != 0 && i % 80 == 0) {
                new_text.append("\n");
            }
            new_text.append(output.charAt(i));
        }
        return new_text.toString();
    }
    public static String add_Padding(String text, int bit_num) {
        // get the size of the text string
        int Size_String = text.getBytes().length;
        // do the padding if needed
        int padding = padd(Size_String, bit_num);
        StringBuilder builder_String = new StringBuilder();
        // copy the text inside a new string builder
        for (int i = 0; i < Size_String; i++) {
            builder_String.append(text.charAt(i));
        }
        // insert the padding if there is any
        if (padding > 0) {
            for (int j = Size_String; j < Size_String+padding; j++) {
                builder_String.append("X");
            }
        }

        return builder_String.toString();
    }

    public static int padd( int length, int num_bit){
        int mod =0;
        // if the number of bits is 32 we mod by 4
        if(num_bit == 32)
            mod =4;
        // if the number of bits is 16 we mod by 2
        else
            mod =2;
        int res=0;
        int len =length;
        // calculate the number of padding
        while(len % mod != 0){
            len = len+1;
            res ++;
        }
        // return the number of padding if number of bits is more than 8 otherwise return 0
        if(num_bit > 8)
            return res;
        else
            return 0;
    }
}
