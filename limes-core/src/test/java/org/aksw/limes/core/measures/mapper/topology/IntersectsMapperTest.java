package org.aksw.limes.core.measures.mapper.topology;

import static org.junit.Assert.assertTrue;

import org.aksw.limes.core.io.cache.ACache;
import org.aksw.limes.core.io.cache.MemoryCache;
import org.aksw.limes.core.io.mapping.AMapping;
import org.aksw.limes.core.io.mapping.MappingFactory;
import org.aksw.limes.core.measures.mapper.AMapper;
import org.junit.Test;

/**
 * @author Kevin Dreßler
 * @since 1.0
 */
public class IntersectsMapperTest {

    @Test
    public void testGetMapping() throws Exception {
        ACache s = new MemoryCache();
        s.addTriple("http://test.com/s/#1", "asWKT", "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))");
        s.addTriple("http://test.com/s/#2", "asWKT", "POLYGON ((-10 -10, 0 10, 10 10, 10 0, -10 -10))");
        // anti-meridian test
        s.addTriple("http://test.com/s/#3", "asWKT", "POLYGON ((-179 0, 179 0, 179 10, -179 10, -179 0))");
        ACache t = new MemoryCache();
        t.addTriple("http://test.com/t/#1", "asWKT", "POLYGON ((-1 -1, -1 11, 11 11, 11 -1, -1 -1))");
        t.addTriple("http://test.com/t/#2", "asWKT", "POLYGON ((-10 -10, -10 -20, -20 -20, -20 -10, -10 -10))");
        t.addTriple("http://test.com/t/#3", "asWKT", "POLYGON ((179.5 0, 179.5 10, 178 10, 178 0, 179.5 0))");
        AMapping referenceMapping = MappingFactory.createMapping(MappingFactory.MappingType.MEMORY_MAPPING);
        referenceMapping.add("http://test.com/s/#2", "http://test.com/t/#2", 1.0d);
        referenceMapping.add("http://test.com/s/#1", "http://test.com/t/#1", 1.0d);
        referenceMapping.add("http://test.com/s/#2", "http://test.com/t/#1", 1.0d);
        referenceMapping.add("http://test.com/s/#3", "http://test.com/t/#3", 1.0d);
        AMapper mapper = new IntersectsMapper();
        assertTrue("Expect mapping generated by IntersectsMapper to be equal to reference mapping",
                mapper.getMapping(s, t, "?x", "?y", "top_within(x.asWKT, y.asWKT)", 1.0d).equals(referenceMapping));
    }
}