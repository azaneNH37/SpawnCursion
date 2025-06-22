package com.azane.spcurs.resource.helper;

import java.util.List;

public interface ITagLike<T>
{
    Class<T> getTagType();

    List<T> getTagList();

    @SuppressWarnings("unchecked")
    default <U> ITagLike<U> castToType(Class<U> type)
    {
        if(getTagType().equals(type))
        {
            return (ITagLike<U>) this;
        }
        throw new ClassCastException("Cannot cast to " + type.getName());
    }

    void absorb(ITagLike<T> other);
}
