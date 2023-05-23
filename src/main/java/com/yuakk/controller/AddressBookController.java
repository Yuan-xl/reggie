package com.yuakk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuakk.common.BaseContext;
import com.yuakk.common.BizException;
import com.yuakk.common.ErrorCodeEnum;
import com.yuakk.pojo.AddressBook;
import com.yuakk.service.AddressBookService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuakk
 */
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Resource
    private AddressBookService addressBookService;

    @GetMapping("/list")
    public List<AddressBook> getAddress(){
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        return addressBookService.list(lambdaQueryWrapper);
    }

    @PostMapping
    public boolean save(@RequestBody AddressBook addressBook){
        if (addressBook!=null){
            addressBook.setUserId(BaseContext.getCurrentId());
            return addressBookService.save(addressBook);
        }
        throw new BizException(400, ErrorCodeEnum.USER_ERROR_A0400);
    }

    @GetMapping("/{id}")
    public AddressBook getAddressBookById(@PathVariable Long id){
        return addressBookService.getById(id);
    }

    @PutMapping
    public boolean updateAddressBook(@RequestBody AddressBook addressBook){
        return addressBookService.updateById(addressBook);
    }

    @DeleteMapping
    public boolean deleteAddressBookByIds(@RequestParam List<Long> ids){
        return addressBookService.removeBatchByIds(ids);
    }

    @PutMapping("/default")
    public boolean changeDefaultAddress(@RequestBody AddressBook addressBook){
        return addressBookService.changeDefaultAddress(addressBook);
    }

    @GetMapping("/default")
    public AddressBook getDefaultAddress(){
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        lambdaQueryWrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBookServiceOne = addressBookService.getOne(lambdaQueryWrapper);
        if (addressBookServiceOne!=null){
            return addressBookServiceOne;
        }else {
            throw new BizException(400, ErrorCodeEnum.USER_ERROR_A0419);
        }
    }

}
