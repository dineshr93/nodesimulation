/* 
 * Copyright (c) 2010-2012 Thiago T. Sá
 * 
 * This file is part of cloudoutput.
 *
 * cloudoutput is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * cloudoutput is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * For more information about your rights as a user of cloudoutput,
 * refer to the LICENSE file or see <http://www.gnu.org/licenses/>.
 */

package cloudoutput.enums;

import cloudoutput.dao.RandomNumberDAO;
import cloudoutput.models.RandomNumber;
import cloudoutput.utils.RandomNumberGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum RandomNumbersFactory {
    
     
    JAVA(0) {

        @Override
        public List<Double> getRandomNumbers(int amount) {
            List<Double> randomNumbers = new ArrayList<Double>();
            Random random = new Random();
            for(int i = 0; i < amount; i++) {
                randomNumbers.add(random.nextDouble());
            }
            return randomNumbers;
        }
    },
    QRBG(1) {

        @Override
        public List<Double> getRandomNumbers(int amount) {
            List<Double> randomNumbers = new ArrayList<Double>();
            try {
                RandomNumberDAO rnDAO = new RandomNumberDAO();
                randomNumbers = rnDAO.getRandomNumbers(amount);
            } catch (Exception e) {
                return null;
            }
            return randomNumbers;
        }
    };
    
    private int code;
    private RandomNumbersFactory(int code) {
        this.code = code;
    }
    public static RandomNumbersFactory getInstance(int code) {
        switch (code) {
            case 1:
                return RandomNumbersFactory.QRBG;
            default: //case 0:
                return RandomNumbersFactory.JAVA;
        }
    }

    /** 
     * Gets the code of this factory's source.
     *
     * @return          the code of this factory's source.
     * @since           1.0
     */        
    public int getCode() {
        return code;
    }
    
    /** 
     * An abstract method to be implemented by every {@link RandomNumbersFactory}.
     * It gets a specific amount of random numbers.
     *
     * @param   amount  the amount of random numbers to be retrieved.
     * @return          a list of random numbers.
     * @since           1.0
     */      
    public abstract List<Double> getRandomNumbers(int amount);

    /** 
     * Gets the alias of this factory.
     *
     * @return  a string containing the alias of this factory.
     * @since   1.0
     */ 
    @Override
    public String toString() {
        switch (getCode()) {
            case 1:
                return "Quantum Random Bit Generator Service";
            default: //case 0:
                return "Java";
        }
    }
}
