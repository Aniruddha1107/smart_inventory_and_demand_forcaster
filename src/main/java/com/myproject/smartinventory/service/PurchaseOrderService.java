package com.myproject.smartinventory.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myproject.smartinventory.dto.PurchaseOrderDTO;
import com.myproject.smartinventory.entity.ChangeType;
import com.myproject.smartinventory.entity.InventoryLog;
import com.myproject.smartinventory.entity.PoStatus;
import com.myproject.smartinventory.entity.Product;
import com.myproject.smartinventory.entity.PurchaseOrder;
import com.myproject.smartinventory.entity.PurchaseOrderItem;
import com.myproject.smartinventory.entity.User;
import com.myproject.smartinventory.repository.InventoryLogRepository;
import com.myproject.smartinventory.repository.ProductRepository;
import com.myproject.smartinventory.repository.PurchaseOrderRepository;

import jakarta.transaction.Transactional;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository poRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryLogRepository inventoryLogRepository;

    @Autowired
    private AlertService alertService;

    @Transactional
    public PurchaseOrder createPO(PurchaseOrderDTO dto, User createdBy) {
        PurchaseOrder po = new PurchaseOrder();
        po.setSupplier(dto.getSupplier());
        po.setStatus(PoStatus.PENDING);
        po.setCreatedBy(createdBy);

        for (PurchaseOrderDTO.PurchaseOrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemDTO.getProductId()));
            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setPurchaseOrder(po);
            item.setProduct(product);
            item.setQuantityOrdered(itemDTO.getQuantityOrdered());
            item.setUnitCost(itemDTO.getUnitCost());
            po.getItems().add(item);
        }
        return poRepository.save(po);
    }

    @Transactional
    public PurchaseOrder receivePO(Long poId, User receivedBy) {
        PurchaseOrder po = poRepository.findById(poId)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found: " + poId));

        if (po.getStatus() != PoStatus.PENDING) {
            throw new RuntimeException("Only PENDING orders can be received.");
        }

        po.setStatus(PoStatus.RECEIVED);
        po.setReceivedAt(LocalDateTime.now());

        for (PurchaseOrderItem item : po.getItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantityOrdered());
            productRepository.save(product);

            InventoryLog log = new InventoryLog();
            log.setProduct(product);
            log.setChangeType(ChangeType.RESTOCK);
            log.setQtyChange(item.getQuantityOrdered());
            log.setUser(receivedBy);
            inventoryLogRepository.save(log);

            alertService.checkAndCreateAlert(product);
        }

        return poRepository.save(po);
    }

    @Transactional
    public PurchaseOrder cancelPO(Long poId) {
        PurchaseOrder po = poRepository.findById(poId)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found: " + poId));
        if (po.getStatus() == PoStatus.RECEIVED) {
            throw new RuntimeException("Cannot cancel an already received order.");
        }
        po.setStatus(PoStatus.CANCELLED);
        return poRepository.save(po);
    }

    public List<PurchaseOrder> getAllPOs() {
        return poRepository.findAllByOrderByCreatedAtDesc();
    }

    public PurchaseOrder getPOById(Long id) {
        return poRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found: " + id));
    }

    public long getPendingCount() {
        return poRepository.countByStatus(PoStatus.PENDING);
    }
}
