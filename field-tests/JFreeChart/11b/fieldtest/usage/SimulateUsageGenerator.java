package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

import java.awt.geom.GeneralPath;

public class SimulateUsageGenerator extends Generator<GeneralPathPair> {

    private static final int MAX_NR_PATH_ELEMS = 6;
    private static final int MAX_VALUE = 20;
    private static final double PROB_SAME_PATH_TWICE = 0.1;
    public SimulateUsageGenerator() {
        super(GeneralPathPair.class);
    }
    public float generateIntegralFloat(SourceOfRandomness random){
        return (float) random.nextInt(0, MAX_VALUE);
    }
    
    public void moveToSingle(GeneralPath path, SourceOfRandomness random){
        float moveX = generateIntegralFloat(random);
        float moveY = generateIntegralFloat(random);
        path.moveTo(moveX, moveY);
    }
    public void addPathElemToSinglePath(GeneralPath path, SourceOfRandomness random){
        switch(random.nextInt(4)) { // choose one of eight subclasses
            case 0:
                moveToSingle(path,random);
                break;
            case 1:
                float lineX = generateIntegralFloat(random);
                float lineY = generateIntegralFloat(random);
                path.lineTo(lineX, lineY);
                break;
            case 2:
                float curveX1 = generateIntegralFloat(random);
                float curveY1 = generateIntegralFloat(random);
                float curveX2 = generateIntegralFloat(random);
                float curveY2 = generateIntegralFloat(random);
                float curveX3 = generateIntegralFloat(random);
                float curveY3 = generateIntegralFloat(random);
                path.curveTo(curveX1,curveY1,curveX2,curveY2, curveX3,curveY3);
                break;
            case 3:
            default:
                float quadX1 = generateIntegralFloat(random);
                float quadY1 = generateIntegralFloat(random);
                float quadX2 = generateIntegralFloat(random);
                float quadY2 = generateIntegralFloat(random);
                path.quadTo(quadX1,quadY1,quadX2,quadY2);
                break;
        }
    }
    
    public void moveToPair(GeneralPathPair pair, SourceOfRandomness random){
        float moveX = generateIntegralFloat(random);
        float moveY = generateIntegralFloat(random);
        pair.p1.moveTo(moveX, moveY);
        pair.p2.moveTo(moveX, moveY);
    }
    public void addPathElemToPair(GeneralPathPair pair, SourceOfRandomness random){
         switch(random.nextInt(4)) { // choose one of eight subclasses
            case 0:
                moveToPair(pair,random);
                break;
            case 1:
                float lineX = generateIntegralFloat(random);
                float lineY = generateIntegralFloat(random);
                pair.p1.lineTo(lineX, lineY);
                pair.p2.lineTo(lineX, lineY);
                break;
            case 2:
                float curveX1 = generateIntegralFloat(random);
                float curveY1 = generateIntegralFloat(random);
                float curveX2 = generateIntegralFloat(random);
                float curveY2 = generateIntegralFloat(random);
                float curveX3 = generateIntegralFloat(random);
                float curveY3 = generateIntegralFloat(random);
                pair.p1.curveTo(curveX1,curveY1,curveX2,curveY2, curveX3,curveY3);
                pair.p2.curveTo(curveX1,curveY1,curveX2,curveY2, curveX3,curveY3);
                break;
            case 3:
            default:
                float quadX1 = generateIntegralFloat(random);
                float quadY1 = generateIntegralFloat(random);
                float quadX2 = generateIntegralFloat(random);
                float quadY2 = generateIntegralFloat(random);
                pair.p1.quadTo(quadX1,quadY1,quadX2,quadY2);
                pair.p2.quadTo(quadX1,quadY1,quadX2,quadY2);
                break;
        }
    
    }
    @Override
    public GeneralPathPair generate(SourceOfRandomness random, GenerationStatus status) {
        GeneralPathPair pair = new GeneralPathPair();
        pair.p1 = new GeneralPath();
        pair.p2 = new GeneralPath();
        boolean generateTheSamePathTwice = random.nextDouble() < PROB_SAME_PATH_TWICE;
        if(generateTheSamePathTwice){
            int nrPathElems = random.nextInt(1, MAX_NR_PATH_ELEMS);
            moveToPair(pair,random); // initial moveto is necessary

            for(int i = 0; i < nrPathElems; i ++){
                addPathElemToPair(pair,random);
            }
        } else{
            int nrPathElems1 = random.nextInt(1, MAX_NR_PATH_ELEMS);
            int nrPathElems2 = random.nextInt(1, MAX_NR_PATH_ELEMS);
            moveToSingle(pair.p1,random);
            for(int i = 0; i < nrPathElems1; i ++){
                addPathElemToSinglePath(pair.p1,random);
            }
            moveToSingle(pair.p2,random);
            for(int i = 0; i < nrPathElems2; i ++){
                addPathElemToSinglePath(pair.p2,random);
            }
        }
        
        return pair; 
    }
}
