package com.yuakk.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuakk.common.BaseContext;
import com.yuakk.mapper.AddressBookMapper;
import com.yuakk.pojo.AddressBook;
import com.yuakk.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author yuakk
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {


    @Override
    public boolean changeDefaultAddress(AddressBook addressBook) {

        LambdaUpdateWrapper<AddressBook> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        lambdaUpdateWrapper.set(AddressBook::getIsDefault, 0);
        this.update(lambdaUpdateWrapper);

        addressBook.setIsDefault(1);
        return this.updateById(addressBook);
    }
}
