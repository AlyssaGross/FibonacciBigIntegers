import java.math.BigInteger;
import java.util.Arrays;

public class MyBigInteger
{
    int Value [];
    int Sign;

    public MyBigInteger()
    {
        Value = new int [1];
        Sign = 1;
    }

    public MyBigInteger(int [] num, int sign)
    {
        Sign = sign;
        Value = num.clone();
    }

    public MyBigInteger(MyBigInteger x)
    {
        this.Value = x.Value.clone();
        this.Sign = x.Sign;
    }

    public MyBigInteger(long x)
    {
        long base = (long)Math.pow(2, 31);

        if(x < 0)
            Sign = -1;
        else
            Sign = 1;

        if( x  < base)
        {
            Value = new int[1];
            Value[0] = (int)(Sign * x);
        }
        else
        {
            Value = new int[2];
            Value[0] = (int)(Sign * (x%base));
            Value[1] = (int)(Sign * (x/base));
        }
    }


    public String toString()
    {
        String hex="";
        int hexDigits = (Value.length) * 8 ;
        int hexNumber [] = new int[hexDigits];
        long temp;
        int maxHex = 0, minHex, power;
        for (int i = 0; i < Value.length; i++)
        {
            if (i > 0) {
                power = (i - 1) % 4;
                temp = (long) Value[i] * (8 / ((long) Math.pow(2, power)));
                if(i > 4 && (i-5)%4 == 0)
                    minHex = maxHex - 2 ;
                else
                    minHex = maxHex - 1;

                maxHex = minHex + 9;
            } else {
                temp = Value[i];
                minHex = maxHex;
                maxHex = 8;
            }

            for (int k = minHex; k < maxHex; k++)
            {
                hexNumber[k] += temp % 16;
                temp = temp / 16;
            }
        }

        while (hexDigits>4 &&(hexNumber[hexDigits-1] == 0 && hexNumber[hexDigits-2]==0 && hexNumber[hexDigits-3]==0 && hexNumber[hexDigits-4]==0))
        {
            hexDigits-= 4;
        }
        for(int j=0; j< hexDigits; j++)
        {
            if (j!= 0 && j%4 == 0)
                hex = ":" + hex ;

            if(hexNumber[j] < 10)
                hex = (char)('0' + hexNumber[j]) + hex ;
            else
                hex = (char)('A' + (hexNumber[j] -10)) + hex ;
        }
        if (Sign == -1)
            hex = "-" + hex;
        return hex;
    }





    MyBigInteger plus(MyBigInteger x)
    {
        MyBigInteger bigA = new MyBigInteger(this);
        MyBigInteger bigB = new MyBigInteger(x);
        MyBigInteger sumBigInteger;
        long base = (long)Math.pow(2,31);
        int ALen = bigA.Value.length;
        int BLen = bigB.Value.length;
        //int sumLen;
        int numDigits = Math.max(ALen, BLen)+1;
        long tempSum;
        int carry = 0;
        //int sumValue [] = new int [numDigits+1];
        int sumValue [] = new int [numDigits];
        //for(int i = 0; i < numDigits-1; i++)
        if(bigA.Sign ==-1 && bigB.Sign == 1)
        {
            bigA.Sign = 1;
            sumBigInteger = bigB.minus(bigA);
        }
        else if (x.Sign == -1 && this.Sign == 1)
        {
            bigB.Sign = 1;
            sumBigInteger = bigA.minus(bigB);
        }
        else {
            for (int i = 0; i < numDigits - 1; i++) {
                if (i < Math.min(ALen, BLen)) {
                    tempSum = (long) bigB.Value[i] + (long) bigA.Value[i] + (long) carry;
                } else if (i < ALen) {
                    tempSum = (long) bigA.Value[i] + (long) carry;
                } else {
                    tempSum = (long) bigB.Value[i] + (long) carry;
                }

                if (tempSum >= base) {
                    tempSum = tempSum - base;
                    sumValue[i] = (int) tempSum;
                    carry = 1;
                } else {
                    sumValue[i] = (int) tempSum;
                    carry = 0;
                }
            }

            if (carry == 1) {
                sumValue[numDigits - 1] = 1;
            }

            while (numDigits > 1 && sumValue[numDigits - 1] == 0)
                numDigits--;

            sumBigInteger = new MyBigInteger(Arrays.copyOf(sumValue, numDigits), this.Sign);
        }

        return sumBigInteger;
    }

    void setValue(int[] val)
    {
        Value = val.clone();
    }
    boolean equalTo(MyBigInteger x)
    {
        int thisLen = this.Value.length;
        int xLen = x.Value.length;

        if(this.Sign != x.Sign)
            return false;
        if (thisLen == xLen)
            while ( thisLen > 0 && this.Value[thisLen-1] == x.Value[thisLen-1])
                thisLen--;

        return thisLen == 0;
    }

    boolean greaterThan(MyBigInteger x)
    {
        int thisLen = this.Value.length;
        int xLen = x.Value.length;

        if(this.Sign != x.Sign)
            return this.Sign == 1;

        if(thisLen != xLen)
            return thisLen > xLen;
        else
        {
            while (thisLen > 1 && this.Value[thisLen-1] == x.Value[thisLen-1])
                thisLen--;
            return this.Value[thisLen-1] > x.Value[thisLen-1];
        }
    }

    boolean lessThan(MyBigInteger x)
    {
        return !this.equalTo(x) && !this.greaterThan(x);
    }

    void increment()
    {

        int index = 0;
        int base = (int)Math.pow(2,31);

        while(index < Value.length && ((long)Value[index] + 1 >= (long)base))
        {
            Value[index] = 0;
            index++;
        }

        if(index == Value.length)
        {
            int newVal [] = new int [index+1];
            newVal[index] = 1;
            this.setValue(newVal);
        }
        else
            Value[index]++;

    }

    void copy (MyBigInteger x)
    {
        Value = x.Value.clone();
        Sign = x.Sign;
    }

    MyBigInteger times (MyBigInteger x)
    {
        MyBigInteger product = new MyBigInteger();
        MyBigInteger intermediate = new MyBigInteger();
        int lenA, lenB, index;
        long digitA, digitB;
        int A[], B[];
        int interProduct[];
        long prod, carry;
        long base = (long)Math.pow(2, 31);

        if(this.Value.length < x.Value.length)
        {
            lenA = x.Value.length;
            A    = x.Value;
            lenB = this.Value.length;
            B    = this.Value;
        }
        else
        {
            lenA = this.Value.length;
            A    = this.Value;
            lenB = x.Value.length;
            B    = x.Value;
        }

        interProduct = new int [lenA+lenB];

        for(int b = 0; b < lenB; b++) {
            index = b;
            carry = 0;
            // pad intermediate product with the appropriate number of 0's
            for(int i = 0; i < index; i++)
                interProduct[i] = 0;
            digitB = B[b];
            for (int a = 0; a < lenA; a++) {
                digitA = A[a];
                prod = digitA * digitB + carry;

                interProduct[index] = (int) (prod%base);
                carry = prod/base;
                index++;
            }
            if(carry > 0)
            {
                interProduct[index] = (int)carry;
            }
            intermediate.setValue(Arrays.copyOf(interProduct, index+1));
            product = product.plus(intermediate);
        }
        product.Sign = this.Sign * x.Sign;

        return product;
    }

    MyBigInteger minus (MyBigInteger x)
    {
        MyBigInteger bigA = new MyBigInteger(this);
        MyBigInteger bigB = new MyBigInteger(x);

        MyBigInteger diffBigInteger;
        long base = (long)Math.pow(2,31);
        int ALen = bigA.Value.length;
        int BLen = bigB.Value.length;
        int numDigits = Math.max(ALen, BLen);
        int diffValue [] = new int [numDigits];
        long A[] = new long[numDigits], B[] = new long [numDigits];
        int temp [];
        int tmp;
        int tmpSign;

        if(bigA.Sign != bigB.Sign)
        {
            tmpSign = bigA.Sign;
            bigA.Sign = 1;
            bigB.Sign = 1;
            diffBigInteger = bigA.plus(bigB);
            diffBigInteger.Sign = tmpSign;
        }
        else if(bigB.greaterThan(bigA))
        {
            diffBigInteger = bigB.minus(bigA);
            diffBigInteger.Sign = -1;
        }
        else
        {
            for (int i = 0; i < numDigits; i++)
            {
                if (i < ALen)
                    A[i] = bigA.Value[i];
                else
                    A[i] = 0;

                if (i < BLen)
                    B[i] = bigB.Value[i];
                else
                    B[i] = 0;
            }


            for (int i = 0; i < numDigits; i++)
            {

                if (A[i] - B[i] < 0)
                {
                    tmp = i + 1;
                    while ( tmp < numDigits-1 && A[tmp] == 0)
                    {
                        tmp++;
                    }

                    while (tmp > i)
                    {
                        A[tmp]--;
                        tmp--;
                        A[tmp] += base;
                    }
                }

                diffValue[i] = (int) (A[i] - B[i]);
            }



            while (numDigits > 1 &&diffValue[numDigits - 1] == 0)
                numDigits--;

            diffBigInteger = new MyBigInteger(Arrays.copyOf(diffValue, numDigits), this.Sign);
        }

        return diffBigInteger;
    }

    int  numberBits()
    {
        int numBits, base;
        numBits = 31 * (Value.length -1);
        base = 1;
        while (base <= Value[Value.length-1])
        {
            numBits++;
            base *= 2;
        }

        return numBits;
    }

    int mod2()
    {
        return Value[0] % 2;
    }

    MyBigInteger divide2()
    {
        long temp;
        long base = (long)Math.pow(2,31);
        int i;
        int len = Value.length;
        for( i = 0; i < len; i ++)
        {
            if (i> 0 && Value[i] %2 == 1)
            {
                temp = (long)Value[i-1] + (long)(base/2);
                Value[i-1] = (int) (temp%base);

                Value[i] = Value[i]/2 + (int)(temp/base);


            }
            else
                Value[i] = Value[i] / 2;
        }

 /*       if(Value[len-1] == 0)
        {
            while (len > 1 && Value[len - 1] == 0)
                len--;

            this.setValue(Arrays.copyOf(Value, len+1));
        }
*/
        return this;
    }

    MyBigInteger timesFaster(MyBigInteger x)
    {
        int lenA, lenB;
        int A1[], A2[], B1[], B2[];
     //   int base [];
       // int upperBase[];

        lenA = this.Value.length;
        lenB = x.Value.length;

        int len = Math.max(lenA, lenB);
        if(len < 2)
        {
            return this.times(x);
        }
        else
        {
            if(len%2 != 0)
                len++;

            int halfLen = len/2;
            A1 = new int[halfLen];
            A2 = new int[halfLen];
            B1 = new int[halfLen];
            B2 = new int[halfLen];

            for (int i = 0; i < len; i++)
            {
                if(i < halfLen)
                {
                    if (i < lenA)
                        A1[i] = this.Value[i];
                    else
                        A1[i] = 0;

                    if (i < lenB)
                        B1[i] = x.Value[i];
                    else
                        B1[i] = 0;
                }
                else
                {
                    if (i < lenA)
                        A2[i-halfLen] = this.Value[i];
                    else
                        A2[i-halfLen] = 0;

                    if (i < lenB)
                        B2[i-halfLen] = x.Value[i];
                    else
                        B2[i-halfLen] = 0;
                }

            }


         //   base = new int[halfLen+1];
           // upperBase = new int [len+1];
          //  base[halfLen] = 1;
           // upperBase[len] = 1;

            //MyBigInteger bigBase = new MyBigInteger(base,1);
            //MyBigInteger bigUpperBase = new MyBigInteger(upperBase,1);

            MyBigInteger bigA1 = new MyBigInteger(A1, this.Sign);
            MyBigInteger bigA2 = new MyBigInteger(A2, this.Sign);
            MyBigInteger bigB1 = new MyBigInteger(B1, x.Sign);
            MyBigInteger bigB2 = new MyBigInteger(B2, x.Sign);

            MyBigInteger z0 = (bigA1.timesFaster(bigB1));
            MyBigInteger z1 = (bigA1.plus(bigA2)).timesFaster(bigB1.plus(bigB2));
            MyBigInteger z2 = (bigA2.timesFaster(bigB2));
            //MyBigInteger result = ((z2.times(bigUpperBase)).plus(((z1.minus(z2)).minus(z0)).times(bigBase))).plus(z0);
            // return ((z2.times(bigUpperBase)).plus(((z1.minus(z2)).minus(z0)).times(bigBase))).plus(z0)
            return (((z2).shift(len)).plus(((z1.minus(z2)).minus(z0))).shift(len/2)).plus(z0);

        }
    }

    MyBigInteger shift(int shift)
    {
        int newlen = Value.length + shift;
        int newValue [] = new int [newlen];
        for(int i = 0; i < newlen; i++)
        {
            if(i < shift)
                newValue[i] = 0;
            else
                newValue[i] = Value[i-shift];
        }
        return new MyBigInteger(newValue, 1);
    }
    static MyBigInteger randomBig(int numDigits)
    {
        MyBigInteger random;
        int arr [] = new int [numDigits];
        for(int i = 0; i < numDigits; i++)
            arr[i] = (int) (Math.random() * Math.pow(2,31));
        random = new MyBigInteger(arr, 1);
        return random;
    }
}
