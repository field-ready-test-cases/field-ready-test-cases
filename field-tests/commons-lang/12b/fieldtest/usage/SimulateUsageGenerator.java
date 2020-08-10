package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class SimulateUsageGenerator extends Generator<RandomStringUtilsArguments> {
    // generate some arguments for RandomStringUtils
    private static final int MAX = 5;
    // don't generate null for now, as the method under test does not terminate
    private static final double NULL_PROBABILITY = 0.0; 

    public SimulateUsageGenerator() {
        super(RandomStringUtilsArguments.class);
    }

    @Override
    public RandomStringUtilsArguments generate(SourceOfRandomness random, GenerationStatus status) {
        RandomStringUtilsArguments args = new RandomStringUtilsArguments();
        args.count = random.nextInt(-2,MAX);
        args.start = random.nextInt(-2,MAX);
        args.end = random.nextInt(-2,MAX);
        args.letters = random.nextBoolean();
        args.numbers = random.nextBoolean();
        
        if(random.nextDouble() < NULL_PROBABILITY){
            args.characters = null;
        } else{
            int nrCharacters = random.nextInt(MAX);
            args.characters = new char[nrCharacters];
            for (int i = 0; i < nrCharacters; i++){
                if(random.nextBoolean())
                    args.characters[i] = random.nextChar('a', 'z');
                else
                    args.characters[i] = random.nextChar('0', '9');
            }
        }
        // needs to make this adjustments, otherwise we may run into infinite loops in the method under test
        if(args.letters && !args.numbers) {
            if(args.characters.length == 0)
                args.characters = new char[1];
            
            args.characters[0] = random.nextChar('a', 'z');
            args.start = 0;
        } else if(!args.letters && args.numbers) {
            if(args.characters.length == 0)
                args.characters = new char[1];
            
            args.characters[0] = random.nextChar('0', '9');
            args.start = 0;
        }
        return args;
    }
}
