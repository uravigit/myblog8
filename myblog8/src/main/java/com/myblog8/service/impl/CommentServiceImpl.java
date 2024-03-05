package com.myblog8.service.impl;


import com.myblog8.entity.Comment;
import com.myblog8.entity.Post;
import com.myblog8.exception.ResourceNotFound;
import com.myblog8.payload.CommentDto;
import com.myblog8.repository.CommentRepository;
import com.myblog8.repository.PostRepository;
import com.myblog8.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepo;
    private PostRepository postRepo;

    private ModelMapper modelMapper;

    public CommentServiceImpl(
            CommentRepository commentRepo,
            PostRepository postRepo,
            ModelMapper modelMapper
    ) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto saveComment(CommentDto dto, long postId) {

        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post not found with id:" + postId)
        );

      //  Comment comment = new Comment();
      //  comment.setId(dto.getId());
      //  comment.setName(dto.getName());
      //  comment.setEmail(dto.getEmail());
      //  comment.setBody(dto.getBody());
      //  comment.setPost(post);

        Comment comment = mapToComment(dto);
        comment.setPost(post);

        Comment savedComment = commentRepo.save(comment);

        CommentDto commentDto  = new CommentDto();

        commentDto.setId(savedComment.getId());
        commentDto.setName(savedComment.getName());
        commentDto.setEmail(savedComment.getEmail());
        commentDto.setBody(savedComment.getBody());

        return commentDto;
    }

    @Override
    public void deleteCommentById(long id) {
        commentRepo.deleteById(id);
    }

    @Override
    public CommentDto updateComment(CommentDto commentDto, long id)
    {
        Comment comment = commentRepo.findById(id).orElseThrow(


                () -> new ResourceNotFound("Comment not Found for id:" + id)
        );
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Comment savedComment = commentRepo.save(comment);


        CommentDto dto  = new CommentDto();

        dto.setId(savedComment.getId());
        dto.setName(savedComment.getName());
        dto.setEmail(savedComment.getEmail());
        dto.setBody(savedComment.getBody());

        return dto;
    }
    public Comment mapToComment(CommentDto dto){
        Comment comment = modelMapper.map(dto, Comment.class);

        // Comment comment = new Comment();
       // comment.setId(dto.getId());
       // comment.setName(dto.getName());
       // comment.setEmail(dto.getEmail());
       // comment.setBody(dto.getBody());
       return comment;

    }

}
