package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.Customer;
import com.bluntsoftware.shirtshop.model.LineItem;
import com.bluntsoftware.shirtshop.model.NamesNumbers;
import com.bluntsoftware.shirtshop.model.PriceProfile;
import com.bluntsoftware.shirtshop.model.PrintLocation;
import com.bluntsoftware.shirtshop.model.QtySize;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemCostingService {

    double getTotal(List<LineItem> items,Customer customer,PriceProfile priceProfile){
        return getSubTotal(items,priceProfile) + getTax(items,customer,priceProfile) - getDiscount(items,priceProfile);
    }

    double getDiscount(List<LineItem> items,PriceProfile priceProfile){
        double total = 0.0;
        for(LineItem t:items){
            total += t.getDiscount() != null ? calculateTotalGarmentPrice(t,priceProfile) * t.getDiscount()/100 : 0.0;
        }
        return total;
    }

    double getTax(List<LineItem> items,Customer customer,PriceProfile priceProfile ){
        if(customer != null && customer.getTaxRate() != null){
            return (getSubTotal(items,priceProfile) - getDiscount(items,priceProfile)) * (customer.getTaxRate()/100);
        }
        return 0.0;
    }

    double getSubTotal(List<LineItem> items,PriceProfile priceProfile){
        double total = 0.0;
        for(LineItem t:items){
            total +=  calculateTotalGarmentPrice(t,priceProfile);
        }
        return total;
    }

    double getProfit(List<LineItem> items,PriceProfile priceProfile){
        double total = 0.0;
        for(LineItem t:items){
                total += calculateTotalGarmentPrice(t,priceProfile) - calculateTotalGarmentCost(t,priceProfile) ;
        }
        return total - getDiscount(items,priceProfile);
    }

    double calculatePricePerGarment(LineItem item,PriceProfile priceProfile){
        double printCosts = 0.0;

       /* if(item.getCostEa() != null){
            return item.getCostEa().doubleValue();
        }*/

      /*  if(priceProfile == null){
            priceProfile = defaultPriceProfile;
        }*/

        for(PrintLocation pl :item.getPrintLocations()){
            printCosts += getPrintLocationPrintCost(pl,priceProfile);
        }

        Double markup  = item.getGarmentMarkupPercentage();

        //priceProfile != null && priceProfile.getGarmentMarkup() != null ? priceProfile.getGarmentMarkup() : 100.00;
        double pricePerGarment =  printCosts +  getCostPerGarment(item,priceProfile)  + (this.getCostPerGarment(item,priceProfile) * (markup/100));
        return Math.round(pricePerGarment * 100) / 100.00;
    }

    public void breakDownByPrintType(LineItem item,PriceProfile priceProfile,Map<String,Map<String,Object>> breakDown){

        if(breakDown == null){
            breakDown = new HashMap<>();
        }

        for(PrintLocation pl :item.getPrintLocations()){
            String printType = pl.getPrintType();
            double plc = getPrintLocationPrintCost(pl,priceProfile) * getGarmentQuantity(item);
            int qty = getGarmentQuantity(item);
            Map<String,Object> plInfo = new HashMap<>();
            if(breakDown.containsKey(printType)){
                plInfo = breakDown.get(printType);
                plc += (double)plInfo.get("cost");
                qty += (int)plInfo.get("qty");
            }
            plInfo.put("cost",plc);
            plInfo.put("qty",qty);
            plInfo.put("avg",plc/qty);
            breakDown.put(printType,plInfo);
        }
    }

    double getPrintLocationPrintCost(PrintLocation pl,PriceProfile priceProfile){
        Map<String, Map<String, BigDecimal>> matrix = priceProfile.getMatrix();
        if(matrix != null){
            Map<String, BigDecimal> printTypes = matrix.get(pl.getPrintType());
            if(printTypes != null){
                BigDecimal cost = printTypes.get(pl.getPrintTypeProperty());
                return cost != null ? cost.doubleValue() : 0.0;
            }
        }
        return 0.0;
    }

    private double getCostPerGarment(LineItem item,PriceProfile priceProfile) {
        if(this.getGarmentQuantity(item) < 1) return 0.0;
        return Math.round(this.calculateTotalGarmentCost(item,priceProfile) / this.getGarmentQuantity(item) * 100) / 100.00;
    }

    private double calculateTotalGarmentCost(LineItem item,PriceProfile priceProfile) {
        return this.getGarmentQuantity(item) * this.calculatePricePerGarment(item,priceProfile);
    }

    private double calculateTotalGarmentPrice( LineItem item,PriceProfile priceProfile){
        return this.getGarmentQuantity(item) * this.calculatePricePerGarment(item,priceProfile);
    }

    int getGarmentQuantity(LineItem lineItem){
        int garmentQty = 0;
        for(QtySize qtySize:lineItem.getSizes().values()){
            garmentQty += qtySize.getQty() != null && qtySize.getQty() > 0 ? qtySize.getQty() : 0;
        }
        List<NamesNumbers> nameNumberList =lineItem.getNamesNumbers();
        if(nameNumberList != null){
            for(NamesNumbers namesNumbers:nameNumberList){
                garmentQty += namesNumbers != null && namesNumbers.getQty()!= null && namesNumbers.getQty() > 0 ? namesNumbers.getQty() : 0;
            }
        }
        return garmentQty;
    }

}
