import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.function.*;

public class FibonacciBigIntegers {
    static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );


    /* define constants */
    static long numberOfTrials = 5;
    static int MAXINPUT = 32768;


    //set up variable to hold folder path and FileWriter/PrintWriter for printing results to a file
    static String ResultsFolderPath = "/home/alyssa/Results/MyBigInteger/"; // pathname to results folder 
    static FileWriter resultsFile;
    static PrintWriter resultsWriter;
    public static void main(String args[])
    {
        ThreadCpuStopWatch TrialStopwatch = new ThreadCpuStopWatch();
        String arithmeticFuncNames [] = {"PLUS", "TIMES", "MINUS", "TIMESFASTER"};
        Consumer<long []> [] sorts = (Consumer<long []> [] )new Consumer<?>[5];
        BiFunction<MyBigInteger, MyBigInteger, MyBigInteger> [] arithmeticFuncs = (BiFunction<MyBigInteger, MyBigInteger, MyBigInteger> [])new BiFunction<?, ?, ?>[4];
        arithmeticFuncs[0] = (MyBigInteger x, MyBigInteger y) -> {return x.plus(y);};
        arithmeticFuncs[1] = (MyBigInteger x, MyBigInteger y) -> {return x.times(y);};
        arithmeticFuncs[2] = (MyBigInteger x, MyBigInteger y) -> {return x.minus(y);};
        arithmeticFuncs[3] = (MyBigInteger x, MyBigInteger y) -> {return x.timesFaster(y);};


        int arr1 [] = {2004318071, 1861152494 ,1};
        MyBigInteger bigI = new MyBigInteger(arr1, 1);

        TrialStopwatch.start();
        // run the fibonacci function on the input
       // System.out.println("fib( 200000 ) --> " + fibFormulaBig(200000).setScale(0, RoundingMode.FLOOR).toString());
        // stop the timer and add to the total time elpsed for the batch of trials
      //  long time = TrialStopwatch.elapsedTime();
       // System.out.println("time to calculate fib( 150000 ) --> " + time);

      //  System.out.println("fib( 2000 ) --> " + fibFormulaBig(10000).setScale(0, RoundingMode.FLOOR).toString());


        //MyBigInteger result;
        //result = fibMatrixBig(new MyBigInteger(38492));
       // System.out.println("fib(3849) = " + result);

        verifyBigIntSimple();
        verifyBigIntLarge();

        for(int func = 3; func < 4; func++)
            for(int run = 0; run < 3; run++)
                runMyBigIntegerExperiment(arithmeticFuncs[func], (arithmeticFuncNames[func]+"-Run"+(run+1)));



    }

    static void verifyBigIntSimple()
    {
        boolean match;
        long min, max, x, y, z;
        String strX, strY, strZ, strBigZ;
        MyBigInteger bigX, bigY, bigZ;

        System.out.println("                      Expression                                                Java Long Result          MyBigInteger Result       Match?");
        System.out.println("                      -------------------------------------------------         --------------------      --------------------      ----------");

        for( int i  =0; i < 16; i ++)
        {
            min = 10000;
            if( i/4 ==1 || i/4 ==3)
                max = (long)Math.pow(2,31);
            else
                max = (long)Math.pow(2,63)/2;
            x = (long) (Math.random() * ((max - min) + 1)) + min;
            y = (long) (Math.random() * ((max - min) + 1)) + min;

            bigX = new MyBigInteger(x);
            bigY = new MyBigInteger(y);

            if(i/4 == 0)
            {
                z = x + y;
                bigZ = bigX.plus(bigY);
            }
            else if ( i/4 == 1)
            {
                z = x * y;
                bigZ = bigX.times(bigY);
            }
            else if (i/4 == 2)
            {
                z = x - y;
                bigZ = bigX.minus(bigY);
            }
            else
            {
                z = x * y;
                bigZ = bigX.timesFaster(bigY);
            }

            strX = format(Long.toHexString(x).toUpperCase());
            strY = format(Long.toHexString(y).toUpperCase());


            if (z < 0)
            {
                z *= -1;
                strZ = "-" + format(Long.toHexString(z).toUpperCase());
            }
            else
                strZ = format(Long.toHexString(z).toUpperCase());

            strBigZ = bigZ.toString();


            match = strZ.equals(strBigZ);

            if (i/4 == 0) {
                System.out.printf("(ADD)                %20s  + %20s              %20s      %20s        %b\n", strX, strY, strZ, strBigZ, match);
            }
            else if (i/4 ==1) {
                System.out.printf("(MULTIPLY)           %20s  * %20s              %20s      %20s        %b\n", strX, strY, strZ, strBigZ, match);
            }
            else if (i/4 == 2) {
                System.out.printf("(SUBTRACT)           %20s  - %20s              %20s      %20s        %b\n", strX, strY, strZ, strBigZ, match);
            }
            else {
                System.out.printf("(MULTIPLY FASTER)    %20s  * %20s              %20s      %20s        %b\n", strX, strY, strZ, strBigZ, match);
            }
        }

    }

    static void verifyBigIntLarge()
    {
        // addition verification
        int arr1  [] = { 1702254437,785050798,1499305433,15147};
        int arr2 [] = { 1161057093,646604966,1364006097, 6697};
        bigIntOperations(arr1, arr2, 0);

        int arr3  [] = { 2147483647,2147483647,2147483647,2147483647,2147483647, 511};
        int arr4 [] = {1};
        bigIntOperations(arr3, arr4, 0);

        int arr5  [] = { 2040109465, 1861152494, 1574821341, 15291};
        int arr6 [] = {2004318071, 61166};
        bigIntOperations(arr5, arr6, 0);

        // simple multiplication verification
        arr4[0] = 2;
        bigIntOperations(arr1, arr4, 1);

        int arr8 [] = {303174162,606348324,1212696648,9474192};
        arr4[0] = 8;
        bigIntOperations(arr8, arr4, 1);

        int arr9 [] = {1, 0, 16};
        int arr10 [] = {306389012 , 2237510};
        bigIntOperations(arr9, arr10, 1);

        // subtraction verification tests
        int arr11 [] = { 124076833, 1822975027, 609};
        int arr12 [] = { 124076833, 1822975027, 33};
        bigIntOperations(arr11, arr12, 2);

        int arr13 [] = { 0, 0, 262144};
        int arr14 [] = { 1212696648, 277909648, 289};
        bigIntOperations(arr13, arr14, 2);

        int arr15 [] = { 1431655765, 10};
        int arr16 [] = { 143165576, 286331153, 139810};
        bigIntOperations(arr15, arr16, 2);

        // same multiplication verification tests as above but with faster version of multiplication
        arr4[0] = 2;
        bigIntOperations(arr1, arr4, 3);

        arr4[0] = 8;
        bigIntOperations(arr8, arr4, 3);

        bigIntOperations(arr9, arr10, 3);


    }

    static void bigIntOperations(int arr1 [], int arr2 [], int operation)
    {
        char op;
        MyBigInteger bigX = new MyBigInteger(arr1, 1);
        MyBigInteger bigY = new MyBigInteger(arr2, 1);
        MyBigInteger bigZ;
        switch (operation) {
            case 0:  bigZ = bigX.plus(bigY);
                    op = '+'; break;
            case 1:  bigZ = bigX.times(bigY);
                    op = '*'; break;
            case 2:  bigZ = bigX.minus(bigY);
                    op = '-'; break;
            default: bigZ = bigX.timesFaster(bigY);
                    op = '*';
        }
        System.out.printf("\n     %57s \n     %c %55s \n     ---------------------------------------------------------\n     %57s \n", bigX, op, bigY, bigZ);
        System.out.println("______________________________________________________________________");
    }

    static void runMyBigIntegerExperiment(BiFunction<MyBigInteger, MyBigInteger, MyBigInteger> arithmeticFunc, String funcNameRun)
    {
        MyBigInteger input1;
        MyBigInteger input2;
        MyBigInteger result;
// add txt file extension to the function name and run number to get the filename
        String filename = funcNameRun + ".txt";

        // open the results file and write an error message if the file doesn't open correctly
        try {
            resultsFile = new FileWriter(ResultsFolderPath + filename);
            resultsWriter = new PrintWriter(resultsFile);
        } catch (Exception e) {
            System.out.println("*****!!!!!  Had a problem opening the results file " + ResultsFolderPath + filename);
            return;
        }

        // create stopwatch for timing an individual trial 
        ThreadCpuStopWatch TrialStopwatch = new ThreadCpuStopWatch();
        // # marks a comment in gnuplot data 
        // print headings for results file of the experiment
        resultsWriter.println("# Experiment Data for " + funcNameRun);
        //resultsWriter.println("#       X               N                T                fib(x)     ");
        resultsWriter.println("# Input Size (digits)   Average Runtime  ");
        resultsWriter.println("# -------------------   ---------------   ");
        resultsWriter.flush();

        // for each input, x in fib(x), we want to test from MININPUT to MAXINPUT
        for (int inputDigits = 1; inputDigits <= MAXINPUT; inputDigits*=2) {
            // print progress message
            System.out.println("Running test for input of  " + inputDigits + " digits ... ");
            System.out.print("    Running trial batch...");
            // reset the elapsed time for the batch to 0
            long batchElapsedTime = 0;


            // repeat for desired number of trials (for a specific input)
            for (long trial = 0; trial < numberOfTrials; trial++) {
                input1 = MyBigInteger.randomBig(inputDigits);
                input2 = MyBigInteger.randomBig(inputDigits);
                // begin timing
                TrialStopwatch.start();
                // run the fibonacci function on the input
                result = arithmeticFunc.apply(input1, input2);
                // stop the timer and add to the total time elpsed for the batch of trials
                batchElapsedTime = batchElapsedTime + TrialStopwatch.elapsedTime();
            }
            // calculate the average time per trial in this batch
            double averageTimePerTrialInBatch = (double) batchElapsedTime / (double) numberOfTrials;

            // print the input, input size in bits, average time per trial in the batch, and the result of fib(input)
            resultsWriter.printf("%17d      %15.2f   \n", inputDigits, averageTimePerTrialInBatch);
            resultsWriter.flush();
            System.out.println(" ....done.");

        }
    }


    // calculate fib(x) using a loop
    static MyBigInteger fibLoopBig(MyBigInteger x)
    {
        MyBigInteger one = new MyBigInteger(1);
        MyBigInteger current, secondToLast, last, i;
        // if x = 0 return 0 and if x = 1 return 1
        if(x.lessThan(one) || x.equalTo(one))
        {
            return x;
        }
        else
        {
            // set secondToLast and last to be the fib(0) and fib(1) ... 0 and 1
            secondToLast = new MyBigInteger();
            last = new MyBigInteger(1);

            // for each value from 2 to x
            for(i = new MyBigInteger(2); i.lessThan(x) || i.equalTo(x); i.increment())
            {
                // calculate the current fib value by adding the previous two values of fib
                current = secondToLast.plus(last);

                // update secondToLast and last with the current value
                secondToLast.copy(last);
                last.copy(current);
            }
            // return last as the result of fib(x)
            return last;
        }
    }

    static MyBigInteger fibMatrixBig(MyBigInteger x)
    {
        MyBigInteger one = new MyBigInteger(1);
        // if x = 0 return 0 and if x = 1 return 1
        if(x.equalTo(one) || x.lessThan(one))
            return x;
        // create the matrix used to calculate fibonacci numbers
        MyBigInteger [][] matrix = {{new MyBigInteger(1), new MyBigInteger(1)},
                                    {new MyBigInteger(1), new MyBigInteger(0)}};
        //long [][] matrix = {{1,1},{1,0}};
        //calculate the matrix to the x+1 power (the results of the matrix are off by one o
        matrix = matrixPower(matrix, x);
        //return the bottom right value in the matrix as the value of fib(x)
        return matrix[0][1];
    }

    static MyBigInteger [][] matrixPower( MyBigInteger[][] x, MyBigInteger y)
    {
        // calculate the number of bits in y
       // int bits = (int)Math.floor( Math.log(y)/Math.log(2)) + 1;
        int bits = y.numberBits();

        // create and initialize matrix secondToLast, and last to hold the intermediate matrices like x x^2 , x^4 , x^8 , x^16 , etc...
        // create and initialize a matrix, result to hold the result of the matrixPower
        MyBigInteger[][] secondToLast   =  new MyBigInteger [x.length][x[0].length];
        MyBigInteger[][] last           =  new MyBigInteger [x.length][x[0].length];
        MyBigInteger[][] result         =  new MyBigInteger [x.length][x[0].length];

        // copy values of matrix x into secondToLast and last matrix
        // for each row in the x matrix
        for(int i=0; i<x.length; i++)
        {
            // copy values of matrix x at row i into secondToLast at row i and last matrix at row i
            secondToLast[i] = x[i].clone();
            last[i] = x[i].clone();
            // set up the result matrix as an identity matrix (acts like 1 with multiplication)
            // 1's along the diagonal at every [i][i] but 0's everywhere else
            result[i][i] = new MyBigInteger(1);
        }

        for(int r = 0; r< result.length; r++) {
            for (int c = 0; c < result[0].length; c++)
                if(r==c)
                    result[r][c] = new MyBigInteger(1);
                else
                    result[r][c] = new MyBigInteger(0);
        }
        // for each bit in y
        for(int i = 0; i < bits; i++)
        {
            // if not the first bit, set secondToLast equal to to last * last
            if(i > 0)
                secondToLast = matrixMultiplication(last,last);

            // if the bit's value is 1 (when y%2 = 0), multiply secondToLast by the result
            if(y.mod2() == 1)
            //if(y%2 == 1)
                result = matrixMultiplication(result, secondToLast);

            // set last equal to secondToLast
            last = secondToLast;
            // move to the next bit by dividing y in half
            y = y.divide2();
        }
        //return the resulting matrix of matrix x^y
        return result;
    }

    static MyBigInteger[][] matrixMultiplication(MyBigInteger [][] first, MyBigInteger [][] second)
    {
        // create matrix to hold the product matrix
        MyBigInteger [][] product = new MyBigInteger [first.length][second[0].length];
        for(int r= 0; r < product.length; r++)
            for(int c = 0; c < product[0].length; c++)
                product[r][c] = new MyBigInteger();

        // for every position [r][c] in the product matrix, calculate the value
        // add firstMatrix at [row][i] * secondMatrix at [i][col] to productMatrix at [row][col] for each i (0 through num cols of first)
        for(int row = 0; row < first.length; row++)
            for (int col = 0; col < second[0].length; col++)
                for (int i = 0; i< first[0].length; i++)
                    product[row][col] = product[row][col].plus(first[row][i].timesFaster(second[i][col]));
        //return product matrix
        return product;
    }

    //calculate fib(X) using the fibonacci formula with java type double (8 byte floating point)
    static double fibFormula(int x)
    {
        // create double to hold phi (the golder ratio)
        // phi = (1+sqrt(5))/2
        double phi = (1.0 + Math.sqrt(5.0))/2.0;
        // return fibonacci formula = (phi^x - phi^-x) / sqrt(5)
        return (Math.pow(phi, x) - Math.pow(phi, -x))/Math.sqrt(5.0);

    }

    //calculate fib(X) using the fibonacci formula with java BigDecimal class
    static BigDecimal fibFormulaBig(int x)
    {
        // set initial precision to 40 to do the calculations to find the actual precision to use
        MathContext precision = new MathContext(40);
        // create BigDecimals for the numbers that will need to be used in BigDecimal calculations
        BigDecimal five = new BigDecimal(5), two = new BigDecimal(2), one = new BigDecimal(1), zero = new BigDecimal(0), ten = new BigDecimal(10);
        // use Big integer to calculate the number of digits because doubles will overflow calculating phi^x much quicker
        // phi = (1+sqrt(5))/2 because
        BigDecimal phi = (one.add(five.sqrt(precision), precision)).divide(two,precision);
        // temp = phi^x
        BigDecimal temp = phi.pow(x);
        // calculate the number of digits expected in the result by converting phi^x to a string and counting the number of digits
        // since there is no log function in bigInt , this should produce roughly the same result as log(phi^x) with a base of 10
        int numDigits = temp.setScale(0, RoundingMode.FLOOR).toString().length();
        // add five extra digits for precision to the numDigits
        precision = new MathContext(numDigits+5);

        // recalculate phi using the appropriate precision for the fibonacci formula
        phi = (one.add(five.sqrt(precision), precision)).divide(two,precision);
        // return fibonacci formula = (phi^x - phi^-x) / sqrt(5)
        return (phi.pow(x, precision).subtract(phi.pow(-1*x, precision), precision)).divide(five.sqrt(precision), precision);
    }

    static String format(String str)
    {
        while(str.length() %4 != 0)
            str = '0' + str;

        for(int i = 3; i < str.length()-4; i +=5)
            str = str.substring(0, i+1) + ":"+str.substring(i+1, str.length());

        return str;
    }



}
