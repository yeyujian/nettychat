package com.yyj.nettychat.service.impl;

import com.yyj.nettychat.entity.FileType;
import com.yyj.nettychat.mapper.UserMapper;
import com.yyj.nettychat.model.User;
import com.yyj.nettychat.service.UserService;
import com.yyj.nettychat.util.FastDFSClient;
import com.yyj.nettychat.util.IdWorker;
import com.yyj.nettychat.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private FastDFSClient fastDFSClient;

    @Value("${fdfs.fileurl}")
    private String fileurl;

    @Override
    public User getUserByName(String name) {
        try {
            User user = new User();
            user.setUsername(name);
            user.setEmail(name);
            return userMapper.selectByName(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查找用户失败");
        }
    }

    @Override
    public User getUserWithPass(User user) {
        try {
            return userMapper.selectWithPass(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查找用户失败");
        }
    }

    @Override
    public boolean register(User user) {
        try {
            // 判断用户是否存在
            if (userMapper.selectByName(user) != null) {
                return false;
            }
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setUserid(idWorker.nextId());
            user.setCreatetime(new Date());
            user.setPhoto("nophoto");
            return userMapper.insert(user) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("注册失败");
        }
    }

    @Override
    public boolean updatePassword(User user) {
        try {
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            return userMapper.updateParameter(user) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("修改密码失败");
        }
    }

    @Override
    public boolean updateNickName(User user) {
        try {
            return userMapper.updateParameter(user) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("修改昵称失败");
        }
    }

    @Override
    public User upload(MultipartFile file, User user) {
        try {
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            if (!(FileType.IMG_TYPE_DMG.equals(suffix.toUpperCase())
                    || FileType.IMG_TYPE_GIF.equals(suffix.toUpperCase())
                    || FileType.IMG_TYPE_JPEG.equals(suffix.toUpperCase())
                    || FileType.IMG_TYPE_JPG.equals(suffix.toUpperCase())
                    || FileType.IMG_TYPE_PNG.equals(suffix.toUpperCase())
                    || FileType.IMG_TYPE_SVG.equals(suffix.toUpperCase()))) {
                return null; // 上传文件不符合
            }
            if ((user = userMapper.selectById(user.getUserid())) == null) {
                throw new RuntimeException("用户不存在");
            }
            String url = fastDFSClient.uploadImages(file);// 在FastDFS上传的时候,会自动生成一个缩略图
            System.out.println("url:" + url);
            String oldPhoto = user.getPhoto();
            if (!oldPhoto.equals("nophoto"))
                fastDFSClient.deleteFile(oldPhoto.substring(oldPhoto.indexOf("group")));
            // 文件名_150x150.后缀
            String[] fileNameList = url.split("\\.");
            String fileName = fileNameList[0];
            String ext = fileNameList[1];
            String picSmallUrl = fileName + "_150x150." + ext;
            user.setPhoto(fileurl + picSmallUrl);
            System.out.println("photo:" + fileurl + picSmallUrl);
            if (userMapper.updateParameter(user) > 0) {
                return userMapper.selectById(user.getUserid());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User findById(String id) {
        return userMapper.selectById(id);
    }

}
