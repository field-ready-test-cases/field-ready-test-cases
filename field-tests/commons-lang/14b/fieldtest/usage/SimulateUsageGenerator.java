package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<CharSequencePair> {
    // this class is taken from StringUtilsEqualsIndexOfTest in the commons-lang project
    private static class CustomCharSequence implements CharSequence {
        private CharSequence seq;

        public CustomCharSequence(CharSequence seq) {
            this.seq = seq;
        }

        public char charAt(int index) {
            return seq.charAt(index);
        }

        public int length() {
            return seq.length();
        }

        public CharSequence subSequence(int start, int end) {
            return new CustomCharSequence(seq.subSequence(start, end));
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof CustomCharSequence)) {
                return false;
            }
            CustomCharSequence other = (CustomCharSequence) obj;
            return seq.equals(other.seq);
        }

        public String toString() {
            return seq.toString();
        }
    }

    public static final int MAX_LENGTH = 20;
    public static final double NULL_PROBABILITY = 0.025;
    // we generate data for an equality check so there is a reasonably chance of seeing equal parameters
    public static final double EQUAL_PROBABILITY = 0.25; 

    public SimulateUsageGenerator() {
        super(CharSequencePair.class);
    }

    public CharSequence toSubClassOfCharSeq(CharSequence cs,SourceOfRandomness random) {
        switch(random.nextInt(0,4)){
            case 0:
                return cs; // do nothing
            case 1:
                return cs.toString();
            case 2:
                return new CustomCharSequence(cs);
            case 3:
                return new StringBuffer(cs);   
            case 4:
            default:
                return new StringBuilder(cs.toString()); // wrap in another StringBuilder        
        }
    }
    public CharSequence generateCharSeq(SourceOfRandomness random) {
        if(random.nextDouble() < NULL_PROBABILITY)
            return null;
        StringBuilder sb = new StringBuilder();
        int length=random.nextInt(MAX_LENGTH); // allow length of zero
    
        for (int i = 0; i < length; i++){
            sb.append(random.nextChar('a','z'));
        }    
                   
        return toSubClassOfCharSeq(sb,random);
    }
    @Override
    public CharSequencePair generate(SourceOfRandomness random, GenerationStatus status) {
        CharSequencePair pair = new CharSequencePair();
        pair.left = generateCharSeq(random);
        if(random.nextDouble() < EQUAL_PROBABILITY){
            pair.right = pair.left;
            if(random.nextBoolean())
                pair.right = toSubClassOfCharSeq(pair.right,random); // wrap once
        }
        else {
            pair.right = generateCharSeq(random);
        }
        return pair;
    }
}
